package com.petservice.main.api.service.Interface;

import com.petservice.main.api.database.dto.BnsVLRequest;
import com.petservice.main.api.database.dto.BnsVLResponse;

import java.util.List;

public interface BnsVLServiceInterface {

  //사업자 인증 요청 저장
  public BnsVLRequest RegisterVLRequest(BnsVLRequest bnsVLRequest);

  //사업자 인증 요청 목록 가져오기(한번에 최대 100개 씩, 완료 안된 것)
  public List<BnsVLRequest> getBnsVLRequestList();

  /*
  사업자 인증 진행(한번에 100 건 or 여러 개의 요청 처리)(PENDING 상태인 것을 먼저 진행)
  (요청이 정상적으로 처리되었으면 해당 요청들을 전부 삭제)
  (사업자 번호로 검색해서 삭제)
  */
  public BnsVLResponse getBnsValidation();

  //사업자 인증 진행(ERROR 상태인 것을 다시 진행)
  public BnsVLResponse getBnsValidationRetry();

  //사업자 인증 진행(NONE 상태인 것을 다시 진행)
  public BnsVLResponse getBnsValidationNoneRetry();

  //사업자 인증 진행
  public BnsVLResponse getBnsResponse(List<BnsVLRequest> requests);

  //사업자 인증 관련 메일 전송
  public void sendAlertMail(String to, String subject, String text);
}
