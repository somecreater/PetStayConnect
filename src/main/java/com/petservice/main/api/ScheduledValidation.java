package com.petservice.main.api;

import com.petservice.main.api.service.Interface.BnsVLServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledValidation {

  private final BnsVLServiceInterface bnsVLService;

  @Scheduled(cron = "0 0 * * * *")
  public void RunValidation(){
    log.info("### 사업자 진위확인 배치 시작 ###");
    try{
      log.info("▶ 신규");
      bnsVLService.getBnsValidation();
      log.info("▶ 오류 재시도");
      bnsVLService.getBnsValidationRetry();
      log.info("▶ NONE 재시도");
      bnsVLService.getBnsValidationNoneRetry();
    }catch (Exception e) {
      e.printStackTrace();
      log.error("사업자 진위 확인 배치 처리중 오류 발생!! {}", e.getMessage());
    }
  }

}
