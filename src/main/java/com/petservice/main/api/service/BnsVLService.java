package com.petservice.main.api.service;

import com.petservice.main.api.database.dto.BnsVLRequest;
import com.petservice.main.api.database.dto.BnsVLResponse;
import com.petservice.main.api.database.entity.BnsVLRequestEntity;
import com.petservice.main.api.database.mapper.BnsVLRequestMapper;
import com.petservice.main.api.database.repository.BnsVLRequestRepository;
import com.petservice.main.api.service.Interface.BnsVLServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BnsVLService implements BnsVLServiceInterface {

  private final BnsVLRequestRepository bnsVLRequestRepository;
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
  사업자 인증 진행(한번에 100 건 or 여러 개의 요청 처리)
  (요청이 정상적으로 처리되었으면 해당 요청들을 전부 삭제)
  (사업자 번호로 검색해서 삭제)
  */
  @Override
  @Transactional
  public BnsVLResponse getBnsValidation(List<BnsVLRequest> bnsVLRequestList) {


    return null;
  }

  //사업자 인증 결과를 즉시 사업자 계정에 반영
  @Override
  @Transactional
  public void UpdateBnsValidation(BnsVLResponse bnsVLResponse) {

  }


}
