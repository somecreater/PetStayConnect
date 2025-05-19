package com.petservice.main.api;

import com.petservice.main.api.service.Interface.BnsVLServiceInterface;
import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.business.database.entity.ReservationStatus;
import com.petservice.main.business.database.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledValidation {

  private final BnsVLServiceInterface bnsVLService;
  private final ReservationRepository reservationRepository;

  @Scheduled(initialDelay = 0, fixedRate = 60 * 60 * 1000)
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

  //2시간 마다 기한 지나고 결제 완료된 예약 COMPLETED로 상태 전환
  @Transactional
  @Scheduled(initialDelay = 0, fixedRate = 120 * 60 * 1000)
  public void ReservationUpdate(){
    LocalDate today = LocalDate.now(ZoneId.of("Asia/Seoul"));
    List<Reservation> expired= reservationRepository
        .findByStatusAndExpired(ReservationStatus.CONFIRMED, today,
            PageRequest.of(0, 200, Sort.by("checkOut").ascending()));

    if(expired.isEmpty()){
      log.info("예약 완료 배치: 처리할 만료 예약 없음");
      return;
    }

    for (Reservation r : expired) {
      try {
        r.setStatus(ReservationStatus.COMPLETED);
      } catch (Exception ex) {
        log.error("예약 {} 상태 전환 실패", r.getId(), ex);
      }
    }
    reservationRepository.saveAll(expired);
    log.info("예약 완료 배치: 총 {}건 처리됨", expired.size());
  }
}
