package com.petservice.main.api.service;

import com.petservice.main.api.database.dto.BnsVLRequest;
import com.petservice.main.api.database.dto.BnsVLRequestList;
import com.petservice.main.api.database.dto.BnsVLResponse;
import com.petservice.main.api.database.dto.BnsVLResult;
import com.petservice.main.api.database.entity.BnsVLRequestEntity;
import com.petservice.main.api.database.entity.RequestStatus;
import com.petservice.main.api.database.mapper.BnsVLRequestMapper;
import com.petservice.main.api.database.repository.BnsVLRequestRepository;
import com.petservice.main.api.service.Interface.BnsVLServiceInterface;
import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.entity.Varification;
import com.petservice.main.business.database.repository.PetBusinessRepository;
import com.petservice.main.common.service.MailServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BnsVLService implements BnsVLServiceInterface {

  @Value("${spring.mail.username}") private String FromMailAddress;
  @Value("${business.api.base-url}") private String baseUrl;
  @Value("${business.api.secret-key}") private String secretKey;

  private final MailServiceInterface mailService;

  private final WebClient webClient;

  private final BnsVLRequestRepository bnsVLRequestRepository;
  private final PetBusinessRepository petBusinessRepository;

  private final BnsVLRequestMapper bnsVLRequestMapper;

  @Override
  @Transactional
  public BnsVLRequest RegisterVLRequest(BnsVLRequest bnsVLRequest, String email) {
    String RegisterNumber= bnsVLRequest.getBNo();

    //이미 요청이 존재할시 중복으로 저장하면 안된다.
    if(!bnsVLRequestRepository.findByBno(RegisterNumber).isEmpty()){
      log.info("요청 핵심 정보(이미 DB에 저장됨): {}   {}   {}"
          ,bnsVLRequest.getBNo(),bnsVLRequest.getStartDt(), bnsVLRequest.getPNm());
      return null;
    }
    //이미 해당 사업자 번호가 서비스 내부에서 인증된 번호면 더 인증하면 안된다.
    if(petBusinessRepository.existByRegistrationNumberAndVarification(
        RegisterNumber, Varification.ISVARIFICATED)){
      log.info("요청 핵심 정보(이미 등록된 정보): {}   {}   {}"
          ,bnsVLRequest.getBNo(),bnsVLRequest.getStartDt(), bnsVLRequest.getPNm());
      return null;
    }

    log.info("요청 핵심 정보: {}   {}   {}",bnsVLRequest.getBNo(),bnsVLRequest.getStartDt(), bnsVLRequest.getPNm());
    BnsVLRequestEntity requestEntity=bnsVLRequestMapper.toEntity(bnsVLRequest);
    requestEntity.setEmail(email);
    requestEntity.setStatus(RequestStatus.PENDING);
    bnsVLRequestRepository.save(requestEntity);

    return bnsVLRequest;
  }

  //사업자 인증 요청 목록 가져오기(한번에 최대 100개 씩, 완료 안된 것)
  @Override
  @Transactional(readOnly = true)
  public List<BnsVLRequest> getBnsVLRequestList() {
    List<BnsVLRequestEntity> requestEntityList = bnsVLRequestRepository.getNoneRequestList();
    return requestEntityList.stream().map(bnsVLRequestMapper::toDTO).toList();
  }

  /*
  사업자 인증 진행(한번에 100 건 or 여러 개의 요청 처리)(PENDING 상태인 것을 먼저 진행)
  (요청이 정상적으로 처리되었으면 해당 요청들을 전부 삭제)
  (사업자 번호로 검색해서 삭제)
  */
  @Override
  @Transactional(noRollbackFor = Exception.class)
  public BnsVLResponse getBnsValidation() throws URISyntaxException {
    List<BnsVLRequestEntity> requestEntityList= bnsVLRequestRepository.findByStatus_PENDING();

    if (requestEntityList == null || requestEntityList.isEmpty()) {
      return null;
    }

    List<BnsVLRequest> requests=requestEntityList.stream()
        .map(bnsVLRequestMapper::toDTO).toList();

    BnsVLResponse response = getBnsResponse(requests);

    if (response.getData().size() != requestEntityList.size()) {
      log.warn("요청 건수({})와 응답 건수({})가 다릅니다.", requestEntityList.size(), response.getData().size());
    }

    if(response.getStatusCode().equals("OK")) {
      for(int i=0 ; i<response.getData().size() ; i++){
        BnsVLResult bnsVLResult=response.getData().get(i);
        String email=requestEntityList.get(i).getEmail();

        //사업자 인증 정상 처리
        if(bnsVLResult.getValid().equals("01")){
          bnsVLRequestRepository.deleteByBno(bnsVLResult.getBNo());
          PetBusiness updateValidation =
            petBusinessRepository.findByRegistrationNumber(bnsVLResult.getBNo());

          try{
            String toMail = email;
            String subject = "사업자 인증이 정상 처리 되었습니다";
            String content =
                "사업자 이름: " + bnsVLResult.getPNm() +
                ", 개업 일자: " + bnsVLResult.getStartDt() +
                ", 사업자 번호: " + bnsVLResult.getBNo() +
                " 실제 사업자임이 확인되었습니다 ^^";
            mailService.sendMail(toMail, subject, content);
          }catch (Exception e){
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }
          updateValidation.setRegistrationNumber(bnsVLResult.getBNo());
          updateValidation.setVarification(Varification.ISVARIFICATED);
          petBusinessRepository.save(updateValidation);
        }
        //사업자 인증이 안됨(잘못된 값)
        else{
          BnsVLRequestEntity bnsVLRequestEntity=bnsVLRequestRepository
              .findByBno(bnsVLResult.getBNo()).orElse(null);
          if(bnsVLRequestEntity == null){
            continue;
          }

          try{
            String toMail = email;
            String subject = "사업자 인증을 실패했습니다!!";
            String content =
                "사업자 이름: " + requestEntityList.get(i).getPnm() +
                ", 개업 일자: " + requestEntityList.get(i).getStartDt() +
                ", 사업자 번호: " + bnsVLResult.getBNo() +
                " 등의 정보들을 다시 확인해주세요!!" +
                " 자동으로 1회 더 사업자 인증을 시도 합니다";
            mailService.sendMail(toMail, subject, content);
          } catch (Exception e) {
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }

          bnsVLRequestEntity.setStatus(RequestStatus.NONE);
          bnsVLRequestRepository.save(bnsVLRequestEntity);
        }

      }
    }else{
      for(BnsVLRequestEntity bnsVLRequest: requestEntityList){
        bnsVLRequest.setStatus(RequestStatus.ERROR);
        bnsVLRequestRepository.save(bnsVLRequest);
      }
      throw new IllegalArgumentException("사업자 진위 인증 API 실행 도중 오류 발생!!");
    }

    return response;
  }

  @Override
  @Transactional(noRollbackFor = Exception.class)
  //사업자 인증 진행(ERROR 상태인 것을 다시 진행)
  public BnsVLResponse getBnsValidationRetry() throws URISyntaxException {
    List<BnsVLRequestEntity> requestEntityList= bnsVLRequestRepository.findByStatus_ERROR();

    if (requestEntityList == null || requestEntityList.isEmpty()) {
      return null;
    }

    List<BnsVLRequest> bnsVLRequestList=requestEntityList.stream()
        .map(bnsVLRequestMapper::toDTO).toList();

    BnsVLResponse bnsVLResponse=getBnsResponse(bnsVLRequestList);

    if(bnsVLResponse.getStatusCode().equals("OK")) {
      for(int i=0 ; i<bnsVLResponse.getData().size();i++){
        BnsVLResult bnsVLResult=bnsVLResponse.getData().get(i);
        String email=requestEntityList.get(i).getEmail();

        //사업자 인증 정상 처리
        if(bnsVLResult.getValid().equals("01")){
          bnsVLRequestRepository.deleteByBno(bnsVLResult.getBNo());
          PetBusiness updateValidation= petBusinessRepository
              .findByRegistrationNumber(bnsVLResult.getBNo());

          try{
            String toMail = email;
            String subject = "사업자 인증이 정상 처리 되었습니다";
            String content =
                "사업자 이름: " + bnsVLResult.getPNm() +
                ", 개업 일자: " + bnsVLResult.getStartDt() +
                ", 사업자 번호: " + bnsVLResult.getBNo() +
                " 실제 사업자임이 확인되었습니다 ^^";
            mailService.sendMail(toMail, subject, content);
          }catch (Exception e){
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }
          updateValidation.setRegistrationNumber(bnsVLResult.getBNo());
          updateValidation.setVarification(Varification.ISVARIFICATED);
          petBusinessRepository.save(updateValidation);

        }
        //사업자 인증이 안됨(잘못된 값)
        else{
          BnsVLRequestEntity bnsVLRequestEntity=bnsVLRequestRepository
              .findByBno(bnsVLResult.getBNo()).orElse(null);
          if(bnsVLRequestEntity == null){
            continue;
          }

          try{
            String toMail = email;
            String subject="사업자 인증을 실패했습니다!!";
            String content=
                "사업자 이름: " + requestEntityList.get(i).getPnm()
              + ", 개업 일자: " + requestEntityList.get(i).getStartDt()
              + ", 사업자 번호: " + bnsVLResult.getBNo()
              + " 등의 정보들을 다시 확인해주세요!!"
              + " 자동으로 1회 더 사업자 인증을 시도 합니다";
            mailService.sendMail(toMail, subject, content);

            bnsVLRequestEntity.setStatus(RequestStatus.NONE);
            bnsVLRequestRepository.save(bnsVLRequestEntity);
          }catch (Exception e){
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }
      }

      }
    }else{
      for(BnsVLRequestEntity bnsVLRequest: requestEntityList){

        try{
          String toMail = bnsVLRequest.getEmail();
          String subject = "사업자 인증을 실패했습니다!!";
          String content =
              "서버 상의 문제입니다!!!" +
                  " 저장된 요청이 데이터 베이스에서 삭제됩니다!!";
          mailService.sendMail(toMail, subject, content);
        }catch (Exception e){
          e.printStackTrace();
          log.error("메일 발송 실패!! {}", e.getMessage());
        }

        bnsVLRequestRepository.deleteByBno(bnsVLRequest.getBno());
      }
      throw new IllegalArgumentException("사업자 진위 인증 API 실행 도중 오류 발생!!");
    }

    return bnsVLResponse;
  }

  //사업자 인증 진행(NONE 상태인 것을 다시 진행)
  @Override
  @Transactional(noRollbackFor = Exception.class)
  public BnsVLResponse getBnsValidationNoneRetry() throws URISyntaxException {
    List<BnsVLRequestEntity> requestEntityList= bnsVLRequestRepository.findByStatus_NONE();

    if (requestEntityList == null || requestEntityList.isEmpty()) {
      return null;
    }

    List<BnsVLRequest> bnsVLRequestList=requestEntityList.stream()
        .map(bnsVLRequestMapper::toDTO).toList();

    BnsVLResponse bnsVLResponse=getBnsResponse(bnsVLRequestList);

    if(bnsVLResponse.getStatusCode().equals("OK")) {
      for(int i=0 ; i<bnsVLResponse.getData().size();i++){
        BnsVLResult bnsVLResult=bnsVLResponse.getData().get(i);
        String email=requestEntityList.get(i).getEmail();

        //사업자 인증 정상 처리
        if(bnsVLResult.getValid().equals("01")){
          bnsVLRequestRepository.deleteByBno(bnsVLResult.getBNo());
          PetBusiness updateValidation= petBusinessRepository
              .findByRegistrationNumber(bnsVLResult.getBNo());

          try{
            String toMail = email;
            String subject = "사업자 인증이 정상 처리 되었습니다";
            String content =
                "사업자 이름: " + bnsVLResult.getPNm() +
                ", 개업 일자: " + bnsVLResult.getStartDt() +
                ", 사업자 번호: " + bnsVLResult.getBNo() +
                " 실제 사업자임이 확인되었습니다 ^^";
            mailService.sendMail(toMail, subject, content);
          }catch (Exception e){
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }
          updateValidation.setRegistrationNumber(bnsVLResult.getBNo());
          updateValidation.setVarification(Varification.ISVARIFICATED);
          petBusinessRepository.save(updateValidation);

        }
        //사업자 인증이 안됨(잘못된 값)(바로 삭제 사용자가 나중에 다시 시도(알림 보내야, 추후 수정))
        else{
          try{
            String toMail =email;
            String subject = "사업자 인증을 실패했습니다!!";
            String content =
                "사업자 이름: " + requestEntityList.get(i).getPnm() +
                ", 개업 일자: " + requestEntityList.get(i).getStartDt() +
                ", 사업자 번호: " + bnsVLResult.getBNo() +
                " 등의 정보들을 다시 확인해주세요!!" +
                " 저장된 요청이 데이터 베이스에서 삭제됩니다!!";
            mailService.sendMail(toMail, subject, content);
          }catch (Exception e){
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }
          bnsVLRequestRepository.deleteByBno(bnsVLResult.getBNo());
        }

      }
    }else{
      for(BnsVLRequestEntity bnsVLRequest: requestEntityList){

        try {
          String toMail = bnsVLRequest.getEmail();
          String subject = "사업자 인증을 실패했습니다!!";
          String content =
              "서버 상의 문제입니다!!!" +
              " 저장된 요청이 데이터 베이스에서 삭제됩니다!!";
          mailService.sendMail(toMail, subject, content);
        }catch (Exception e){
          e.printStackTrace();
          log.error("메일 발송 실패!! {}", e.getMessage());
        }

        bnsVLRequestRepository.deleteByBno(bnsVLRequest.getBno());
      }
      throw new IllegalArgumentException("사업자 진위 인증 API 실행 도중 오류 발생!!");
    }

    return bnsVLResponse;
  }

  @Override
  @Transactional
  public BnsVLResponse getBnsResponse(List<BnsVLRequest> requests) throws URISyntaxException {

    requests.forEach(
      r -> log.info("Calling validate: b_no={} start_dt={} p_nm={}", r.getBNo(), r.getStartDt(),
          r.getPNm()));

    WebClient webClient =
      WebClient.builder().baseUrl(baseUrl)                                  // 기본 URL 세팅
          .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE).build();

    String uriString = String.format("%s?serviceKey=%s&returnType=JSON",
        baseUrl, secretKey);

    URI uri = new URI(uriString);

    BnsVLRequestList payload = new BnsVLRequestList(requests);

    BnsVLResponse response = webClient.post().uri(uri).bodyValue(payload)
          .retrieve().bodyToMono(BnsVLResponse.class).block();
    return response;
  }

}
