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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BnsVLService implements BnsVLServiceInterface {

  @Value("${spring.mail.username}") private String FromMailAddress;
  @Value("${business.api.base-url}") private String baseUrl;

  private final WebClient webClient;

  private final JavaMailSender mailSender;

  private final BnsVLRequestRepository bnsVLRequestRepository;
  private final PetBusinessRepository petBusinessRepository;

  private final BnsVLRequestMapper bnsVLRequestMapper;

  @Override
  @Transactional
  public BnsVLRequest RegisterVLRequest(BnsVLRequest bnsVLRequest) {
    String RegisterNumber= bnsVLRequest.getBNo();

    //이미 요청이 존재할시 중복으로 저장하면 안된다.
    if(bnsVLRequestRepository.findByBno(RegisterNumber)!=null){
      return null;
    }

    BnsVLRequestEntity requestEntity=bnsVLRequestMapper.toEntity(bnsVLRequest);
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
  public BnsVLResponse getBnsValidation() {
    List<BnsVLRequestEntity> requestEntityList= bnsVLRequestRepository.findByStatus_PENDING();

    if (requestEntityList == null || requestEntityList.isEmpty()) {
      return null;
    }

    List<BnsVLRequest> requests=requestEntityList.stream()
        .map(bnsVLRequestMapper::toDTO).toList();

    BnsVLResponse response = getBnsResponse(requests);

    if(response.getStatusCode().equals("OK")) {
      for (BnsVLResult bnsVLResult : response.getData()) {

        //사업자 인증 정상 처리
        if(bnsVLResult.getValid().equals("01")){
          bnsVLRequestRepository.deleteByBno(bnsVLResult.getBNo());
          PetBusiness updateValidation =
            petBusinessRepository.findByRegistrationNumber(bnsVLResult.getBNo());

          try{
            String toMail = updateValidation.getUser().getEmail();
            String subject = "사업자 인증이 정상 처리 되었습니다";
            String content =
                "사업자 이름: " + bnsVLResult.getPNm() +
                ", 개업 일자: " + bnsVLResult.getStartDt() +
                ", 사업자 번호: " + updateValidation.getRegistrationNumber() +
                " 실제 사업자임이 확인되었습니다 ^^";
            sendAlertMail(toMail, subject, content);
          }catch (Exception e){
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }

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

          PetBusiness Validation= petBusinessRepository
              .findByRegistrationNumber(bnsVLResult.getBNo());

          try {
            String toMail = Validation.getUser().getEmail();
            String subject = "사업자 인증을 실패했습니다!!";
            String content =
                "사업자 이름: " + bnsVLResult.getPNm() +
                ", 개업 일자: " + bnsVLResult.getStartDt() +
                ", 사업자 번호: " + bnsVLResult.getBNo() +
                " 등의 정보들을 다시 확인해주세요!!" +
                " 자동으로 1회 더 사업자 인증을 시도 합니다";
            sendAlertMail(toMail, subject, content);
          } catch (Exception e) {
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }

          bnsVLRequestEntity.setStatus(RequestStatus.NONE);
          bnsVLRequestRepository.save(bnsVLRequestEntity);
        }

      }
    }else{
      for(BnsVLRequest bnsVLRequest: requests){
        BnsVLRequestEntity bnsVLRequestEntity=bnsVLRequestRepository
            .findByBno(bnsVLRequest.getBNo()).orElse(null);
        if(bnsVLRequestEntity == null){
          continue;
        }
        bnsVLRequestEntity.setStatus(RequestStatus.ERROR);
        bnsVLRequestRepository.save(bnsVLRequestEntity);
      }
      throw new IllegalArgumentException("사업자 진위 인증 API 실행 도중 오류 발생!!");
    }

    return response;
  }

  @Override
  @Transactional(noRollbackFor = Exception.class)
  //사업자 인증 진행(ERROR 상태인 것을 다시 진행)
  public BnsVLResponse getBnsValidationRetry(){
    List<BnsVLRequestEntity> requestEntityList= bnsVLRequestRepository.findByStatus_ERROR();

    if (requestEntityList == null || requestEntityList.isEmpty()) {
      return null;
    }

    List<BnsVLRequest> bnsVLRequestList=requestEntityList.stream()
        .map(bnsVLRequestMapper::toDTO).toList();

    BnsVLResponse bnsVLResponse=getBnsResponse(bnsVLRequestList);

    if(bnsVLResponse.getStatusCode().equals("OK")) {
      for (BnsVLResult bnsVLResult : bnsVLResponse.getData()) {

        //사업자 인증 정상 처리
        if(bnsVLResult.getValid().equals("01")){
          bnsVLRequestRepository.deleteByBno(bnsVLResult.getBNo());
          PetBusiness updateValidation= petBusinessRepository
              .findByRegistrationNumber(bnsVLResult.getBNo());

          try {
            String toMail = updateValidation.getUser().getEmail();
            String subject = "사업자 인증이 정상 처리 되었습니다";
            String content =
                "사업자 이름: " + bnsVLResult.getPNm() +
                ", 개업 일자: " + bnsVLResult.getStartDt() +
                ", 사업자 번호: " + bnsVLResult.getBNo() +
                " 실제 사업자임이 확인되었습니다 ^^";
            sendAlertMail(toMail, subject, content);
          }catch (Exception e){
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }
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

          PetBusiness Validation= petBusinessRepository
              .findByRegistrationNumber(bnsVLResult.getBNo());

          try{
            String toMail=Validation.getUser().getEmail();
            String subject="사업자 인증을 실패했습니다!!";
            String content=
                "사업자 이름: " + bnsVLResult.getPNm()
              + ", 개업 일자: " + bnsVLResult.getStartDt()
              + ", 사업자 번호: " + bnsVLResult.getBNo()
              + " 등의 정보들을 다시 확인해주세요!!"
              + " 자동으로 1회 더 사업자 인증을 시도 합니다";
            sendAlertMail(toMail, subject, content);

            bnsVLRequestEntity.setStatus(RequestStatus.NONE);
            bnsVLRequestRepository.save(bnsVLRequestEntity);
          }catch (Exception e){
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }
      }

      }
    }else{
      for(BnsVLRequest bnsVLRequest: bnsVLRequestList){

        PetBusiness Validation= petBusinessRepository
            .findByRegistrationNumber(bnsVLRequest.getBNo());
        try {
          String toMail = Validation.getUser().getEmail();
          String subject = "사업자 인증을 실패했습니다!!";
          String content =
              "서버 상의 문제입니다!!!" +
                  " 저장된 요청이 데이터 베이스에서 삭제됩니다!!";
          sendAlertMail(toMail, subject, content);
        }catch (Exception e){
          e.printStackTrace();
          log.error("메일 발송 실패!! {}", e.getMessage());
        }

        bnsVLRequestRepository.deleteByBno(bnsVLRequest.getBNo());
      }
      throw new IllegalArgumentException("사업자 진위 인증 API 실행 도중 오류 발생!!");
    }

    return bnsVLResponse;
  }

  //사업자 인증 진행(NONE 상태인 것을 다시 진행)
  @Override
  @Transactional(noRollbackFor = Exception.class)
  public BnsVLResponse getBnsValidationNoneRetry(){
    List<BnsVLRequestEntity> requestEntityList= bnsVLRequestRepository.findByStatus_NONE();

    if (requestEntityList == null || requestEntityList.isEmpty()) {
      return null;
    }

    List<BnsVLRequest> bnsVLRequestList=requestEntityList.stream()
        .map(bnsVLRequestMapper::toDTO).toList();

    BnsVLResponse bnsVLResponse=getBnsResponse(bnsVLRequestList);

    if(bnsVLResponse.getStatusCode().equals("OK")) {
      for (BnsVLResult bnsVLResult : bnsVLResponse.getData()) {

        //사업자 인증 정상 처리
        if(bnsVLResult.getValid().equals("01")){
          bnsVLRequestRepository.deleteByBno(bnsVLResult.getBNo());
          PetBusiness updateValidation= petBusinessRepository
              .findByRegistrationNumber(bnsVLResult.getBNo());

          try {
            String toMail = updateValidation.getUser().getEmail();
            String subject = "사업자 인증이 정상 처리 되었습니다";
            String content =
                "사업자 이름: " + bnsVLResult.getPNm() +
                ", 개업 일자: " + bnsVLResult.getStartDt() +
                ", 사업자 번호: " + bnsVLResult.getBNo() +
                " 실제 사업자임이 확인되었습니다 ^^";
            sendAlertMail(toMail, subject, content);
          }catch (Exception e){
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }

          updateValidation.setVarification(Varification.ISVARIFICATED);
          petBusinessRepository.save(updateValidation);

        }
        //사업자 인증이 안됨(잘못된 값)(바로 삭제 사용자가 나중에 다시 시도(알림 보내야, 추후 수정))
        else{
          PetBusiness Validation= petBusinessRepository
              .findByRegistrationNumber(bnsVLResult.getBNo());

          try {
            String toMail = Validation.getUser().getEmail();
            String subject = "사업자 인증을 실패했습니다!!";
            String content =
                "사업자 이름: " + bnsVLResult.getPNm() +
                ", 개업 일자: " + bnsVLResult.getStartDt() +
                ", 사업자 번호: " + bnsVLResult.getBNo() +
                " 등의 정보들을 다시 확인해주세요!!" +
                " 저장된 요청이 데이터 베이스에서 삭제됩니다!!";
            sendAlertMail(toMail, subject, content);
          }catch (Exception e){
            e.printStackTrace();
            log.error("메일 발송 실패!! {}", e.getMessage());
          }
          bnsVLRequestRepository.deleteByBno(bnsVLResult.getBNo());
        }

      }
    }else{
      for(BnsVLRequest bnsVLRequest: bnsVLRequestList){

        PetBusiness Validation= petBusinessRepository
            .findByRegistrationNumber(bnsVLRequest.getBNo());
        try {
          String toMail = Validation.getUser().getEmail();
          String subject = "사업자 인증을 실패했습니다!!";
          String content =
              "서버 상의 문제입니다!!!" +
              " 저장된 요청이 데이터 베이스에서 삭제됩니다!!";
          sendAlertMail(toMail, subject, content);
        }catch (Exception e){
          e.printStackTrace();
          log.error("메일 발송 실패!! {}", e.getMessage());
        }

        bnsVLRequestRepository.deleteByBno(bnsVLRequest.getBNo());
      }
      throw new IllegalArgumentException("사업자 진위 인증 API 실행 도중 오류 발생!!");
    }

    return bnsVLResponse;
  }

  @Override
  @Transactional
  public BnsVLResponse getBnsResponse(List<BnsVLRequest> requests){

    UriComponentsBuilder uriBuilder= UriComponentsBuilder
        .fromUriString(baseUrl)
        .encode(StandardCharsets.UTF_8);

    URI uri= uriBuilder.build().toUri();

    BnsVLRequestList bnsVLRequestList=new BnsVLRequestList(requests);

    BnsVLResponse response = webClient.post()
        .uri(uri)
        .bodyValue(bnsVLRequestList)
        .retrieve()
        .bodyToMono(BnsVLResponse.class)
        .block();
    return response;
  }

  @Override
  //사업자 인증 관련 메일 전송
  public void sendAlertMail(String to, String subject, String text){
      SimpleMailMessage message = new SimpleMailMessage();
      message.setFrom(FromMailAddress);  // 발신자
      message.setTo(to);
      message.setSubject(subject);
      message.setText(text);

      mailSender.send(message);
  }

}
