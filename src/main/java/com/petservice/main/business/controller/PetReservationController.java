package com.petservice.main.business.controller;


import com.petservice.main.business.service.Interface.PetReservationServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.dto.PetDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pets/reservation")
@RequiredArgsConstructor
public class PetReservationController {

  private final PetReservationServiceInterface service;

  @GetMapping("/{reservation_id}")
  public ResponseEntity<?> getPetByReservationId(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("reservation_id") Long reservation_id){
    Map<String,Object> response = new HashMap<>();

    List<PetDTO> pets=service.getPetListByReservation(reservation_id);

    if(pets != null){
      response.put("result",true);
      response.put("message", "예약된 펫목록 입니다.");
      response.put("pets",pets);
    }else{
      response.put("result",false);
      response.put("message", "예약된 펫목록을 가져오는 것에 실패하였습니다.");
    }
    return ResponseEntity.ok(response);
  }
}
