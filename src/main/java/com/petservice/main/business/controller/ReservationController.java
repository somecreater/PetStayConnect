package com.petservice.main.business.controller;

import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.business.database.dto.ReservationRequest;
import com.petservice.main.business.service.Interface.PetBusinessServiceInterface;
import com.petservice.main.business.service.Interface.ReservationServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/api/provider")
@RequiredArgsConstructor
public class ReservationController {

  private final ReservationServiceInterface service;
  private final PetBusinessServiceInterface businessService;

  @PostMapping("/{provider_id}/reservation")
  public ResponseEntity<?> registerReservation(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("provider_id") Long provider_id,
      @RequestBody ReservationRequest request){
    Map<String,Object> result = new HashMap<>();

    PetBusinessDTO petBusinessDTO = businessService.getBusinessDto(provider_id);
    if(!Objects.equals(petBusinessDTO.getId(), provider_id)){
      result.put("result",false);
      result.put("message","잘못된 데이터 입니다!");
      return ResponseEntity.ok(result);
    }
    ReservationDTO reservationDTO=service.registerReservation(request);

    result.put("result",true);
    result.put("message","정상적으로 예약이 등록되었습니다.");
    result.put("reservation",reservationDTO);
    result.put("business_name",petBusinessDTO.getBusinessName());
    result.put("price", petBusinessDTO.getMaxPrice()
        * reservationDTO.getPetReservationDTOList().size());

    return ResponseEntity.ok(result);
  }

  @GetMapping("/consumer/reservation")
  public ResponseEntity<?> reservationListByConsumer(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size ){
    Map<String,Object> result = new HashMap<>();


    List<ReservationDTO> reservationDTOS=service.getReservationList(principal.getUsername());

    if(reservationDTOS != null){
      result.put("result",true);
      result.put("message","총 "+reservationDTOS.size()+" 건의 결과를 가지고 왔습니다.");
      result.put("reservations",reservationDTOS);
    }else{
      result.put("result",false);
      result.put("message","결과가 존재하지 않거나 가져오는 것을 실패했습니다.");
    }

    return ResponseEntity.ok(result);
  }

  @GetMapping("/provider/reservation")
  public ResponseEntity<?> reservationListByProvider(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size ){
    Map<String,Object> result = new HashMap<>();
    PetBusinessDTO petBusinessDTO=businessService.getBusinessDtoByUserLoginId(
        principal.getUsername());
    List<ReservationDTO> reservationDTOS=service
        .getReservationListByBusiness(petBusinessDTO.getId());

    if(reservationDTOS != null){
      result.put("result",true);
      result.put("message","총 "+reservationDTOS.size()+" 건의 결과를 가지고 왔습니다.");
      result.put("reservations",reservationDTOS);
    }else{
      result.put("result",false);
      result.put("message","결과가 존재하지 않거나 가져오는 것을 실패했습니다.");
    }
    return ResponseEntity.ok(result);
  }

  //일반 회원 전용
  @GetMapping("/reservation/{reservation_id}")
  public ResponseEntity<?> reservationById(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("reservation_id") Long reservation_id){
    Map<String,Object> result = new HashMap<>();

    ReservationDTO reservationDTO=service.getReservation(principal.getUsername(), reservation_id);

    if(reservationDTO != null){
      result.put("result", true);
      result.put("message", "예약을 가져옵니다.");
      result.put("reservation", reservationDTO);
    }else{
      result.put("result",false);
      result.put("message","예약이 존재하지 않습니다.");
    }

    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/reservation/{reservation_id}")
  public ResponseEntity<?> reservationDelete(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("reservation_id") Long reservation_id){
    Map<String,Object> result = new HashMap<>();

    ReservationDTO exReservation= service.getReservationById(reservation_id);
    if(exReservation ==null){
      result.put("result", false);
      result.put("message", "예약이 존재하지 않습니다.");
      return ResponseEntity.ok(result);
    }

    ReservationDTO deleteReservation= service.deleteReservation(exReservation);
    if(deleteReservation !=null){
      result.put("result",true);
      result.put("message","예약이 취소(삭제) 되었습니다.");
      result.put("reservation",deleteReservation);
    }else{
      result.put("result",false);
      result.put("message","예약이 취소되지 않았습니다(이미 지난 예약 일수도 있습니다!)");
    }
    return ResponseEntity.ok(result);
  }

  @PutMapping("/reservation/{reservation_id}")
  public ResponseEntity<?> reservationUpdate(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("reservation_id") Long reservation_id,
      @RequestBody ReservationDTO reservationDTO){
    Map<String,Object> result = new HashMap<>();
    if(!Objects.equals(reservation_id, reservationDTO.getId())){
      result.put("result",false);
      result.put("message","데이터가 부적절합니다.");
      return ResponseEntity.ok(result);
    }

    ReservationDTO updateReservation= service.updateReservation(reservationDTO);
    if(updateReservation!=null){
      result.put("result",true);
      result.put("message","예약이 수정되었습니다.");
      result.put("reservation",updateReservation);
    }else{
      result.put("result",false);
      result.put("message","예약 수정을 실패했습니다!");
    }

    return ResponseEntity.ok(result);
  }
}
