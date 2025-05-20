package com.petservice.main.business.service;

import com.petservice.main.business.database.dto.PetReservationDTO;
import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.business.database.dto.ReservationRequest;
import com.petservice.main.business.database.entity.*;
import com.petservice.main.business.database.mapper.ReservationMapper;
import com.petservice.main.business.database.repository.PetBusinessRepository;
import com.petservice.main.business.database.repository.PetBusinessRoomRepository;
import com.petservice.main.business.database.repository.ReservationRepository;
import com.petservice.main.business.service.Interface.PetReservationServiceInterface;
import com.petservice.main.business.service.Interface.ReservationServiceInterface;
import com.petservice.main.payment.database.dto.PaymentCancelRequestDTO;
import com.petservice.main.payment.database.dto.PaymentDTO;
import com.petservice.main.payment.database.entity.Payment;
import com.petservice.main.payment.service.PaymentServiceInterface;
import com.petservice.main.pet.service.PetServiceInterface;
import com.petservice.main.user.database.dto.PetDTO;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.repository.PetRepository;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationServiceInterface {

  private final ReservationRepository reservationRepository;
  private final PetBusinessRepository petBusinessRepository;
  private final PetBusinessRoomRepository petBusinessRoomRepository;
  private final PetRepository petRepository;

  private final UserRepository userRepository;
  private final ReservationMapper reservationMapper;

  private final PetServiceInterface petService;
  private final PetReservationServiceInterface PetReservationService;
  private final PaymentServiceInterface paymentService;

  @Override
  @Transactional(readOnly = true)
  public Page<ReservationDTO> getReservationList(String user_login_id, Pageable pageable) {
    Page<Reservation> reservationList= reservationRepository.findByUser_UserLoginId(user_login_id, pageable);
    if(reservationList.isEmpty()){
      return null;
    }
    return reservationList.map(reservationMapper::toDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ReservationDTO> getReservationListByBusiness(Long Business_id, Pageable pageable){
    Page<Reservation> reservationList= reservationRepository.findByPetBusiness_Id(Business_id, pageable);
    if(reservationList.isEmpty()){
      return null;
    }
    return reservationList.map(reservationMapper::toDTO);
  }

  @Override
  @Transactional(readOnly = true)
  public ReservationDTO getReservation(String user_login_id, Long ReservationId) {
    Reservation reservation=reservationRepository.findById(ReservationId).orElse(null);
    ReservationDTO reservationDTO=null;
    if(reservation != null ){
      reservationDTO=reservationMapper.toDTO(reservation);
    }
    if(!Objects.equals(Objects.requireNonNull(reservationDTO).getUserLoginId(), user_login_id)){
      return null;
    }
    return reservationDTO;
  }

  @Override
  @Transactional(readOnly = true)
  public ReservationDTO getReservationByBusiness(String RegisterNumber, Long ReservationId){
    Reservation reservation=reservationRepository.findById(ReservationId).orElse(null);
    ReservationDTO reservationDTO=null;
    if(reservation != null ){
      reservationDTO=reservationMapper.toDTO(reservation);
    }
    if(!Objects.equals(Objects.requireNonNull(reservationDTO).getPetBusinessRegisterNumber(),
        RegisterNumber)){
      return null;
    }
    return reservationDTO;
  }

  @Override
  @Transactional(readOnly = true)
  public ReservationDTO getReservationById(Long ReservationId){
    Reservation reservation=reservationRepository.findById(ReservationId).orElse(null);
    ReservationDTO reservationDTO=null;
    if(reservation != null ){
      reservationDTO=reservationMapper.toDTO(reservation);
    }

    return reservationDTO;
  }

  /*
  예약 진행 (일단 결제는 진행안하고 예약만 서버 상에 등록(PENDING 상태로 설정))
  여러 확인 필요(중복(다른 예약, 본인 애완동물) 여부, 동 시간대 예약, 사업자 상태 확인 필요)
  최소 예약 시도 날짜 부터 3일 이후로만 예약 가능하도록 설정
  */
  @Override
  @Transactional
  public ReservationDTO registerReservation(ReservationRequest reservationRequest) {
    Reservation reservation = new Reservation();
    LocalDate today = LocalDate.now().plusDays(3);

    User user = userRepository.findByUserLoginId(reservationRequest.getUser_login_id())
        .orElse(null);
    PetBusiness petBusiness = petBusinessRepository.findByRegistrationNumber(
        reservationRequest.getBusiness_register_number());
    PetBusinessRoom petBusinessRoom = null;
    if(reservationRequest.getRoomType() != null) {
      petBusinessRoom = petBusinessRoomRepository.findByRoomTypeAndPetBusiness_RegistrationNumber(
          reservationRequest.getRoomType(), reservationRequest.getBusiness_register_number()
      );
    }

    if(user == null || petBusiness == null
        || reservationRequest.getCheckIn() == null
        || reservationRequest.getCheckOut() ==null
        || reservationRequest.getPetDTOList() == null){
      log.error("부적절한 값입니다!!!");
      throw new IllegalArgumentException("부적절한 값입니다!!!");
    }
    if(!reservationRequest.getCheckIn().isAfter(today)
        || !reservationRequest.getCheckOut().isAfter(today)
        || !reservationRequest.getCheckOut().isAfter(reservationRequest.getCheckIn())){
      log.error("부적절한 기간 선택입니다!!!");
      throw new IllegalArgumentException("부적절한 기간 선택입니다!!!");
    }

    //단 한마리의 펫이라도 해당 회원의 펫이 아니거나, 중복된 예약이 존재하면 안된다.
    for(PetDTO petDTO:reservationRequest.getPetDTOList()){

      if(!petService.isUserPet(reservationRequest.getUser_login_id(), petDTO.getId())){
        log.error("해당 회원의 애완 동물이 아닙니다!!!");
        throw new IllegalArgumentException("해당 회원의 애완 동물이 아닙니다!!!");
      }

      if(reservationRepository.existsByCheckInAndCheckOut(reservationRequest.getUser_login_id(),
          reservationRequest.getCheckIn(),reservationRequest.getCheckOut(),petDTO.getId())){
        log.error("{} 애완동물에 예약이 존재합니다!!!", petDTO.getName());
        throw new IllegalArgumentException(petDTO.getName()+" 애완동물에 예약이 존재합니다!!!");
      }
    }
    try {
      reservation.setUser(user);
      reservation.setPetBusiness(petBusiness);
      reservation.setPetBusinessRoom(petBusinessRoom);
      reservation.setCheckIn(reservationRequest.getCheckIn());
      reservation.setCheckOut(reservationRequest.getCheckOut());
      reservation.setBusinessRequestInfo(reservationRequest.getBusinessRequestInfo());
      reservation.setSpecialRequests(reservationRequest.getSpecialRequests());
      reservation.setPeriod((int) ChronoUnit.DAYS.between(reservationRequest.getCheckIn(),
          reservationRequest.getCheckOut()));
      reservation.setStatus(ReservationStatus.PENDING);

      Reservation newReservation = reservationRepository.save(reservation);
      ReservationDTO newReservationDto = reservationMapper.toDTO(newReservation);
      List<PetReservationDTO> petReservations = new ArrayList<>();
      for (PetDTO petDTO : reservationRequest.getPetDTOList()) {
        petReservations.add(
            PetReservationService.registerPetReservation(petDTO, newReservationDto));
      }

      newReservationDto.setPetReservationDTOList(petReservations);

      return newReservationDto;

    }catch (Exception e){
      e.printStackTrace();
      log.error("얘기치 못한 오류 발생!!! {}", e.getMessage());
      throw new IllegalArgumentException("얘기치 못한 오류 발생!!!");
    }
  }

  //예약 수정(단 이미 결제까지 진행된 예약과, 아직 결제가 안된 예약은 별도로 처리,)
  //변경 가능한 요소 (방, 특별 요구 사항, 사업자 요구사항, 기간)
  @Override
  @Transactional
  public ReservationDTO updateReservation(ReservationDTO reservationDTO) {
    Reservation updateReservation =
        reservationRepository.findById(reservationDTO.getId()).orElse(null);

    PetBusinessRoom room= petBusinessRoomRepository.findByIdAndPetBusiness_Id(
        reservationDTO.getPetBusinessRoomId(),reservationDTO.getPetBusinessId());

    if(updateReservation == null || !UpdateValidation(reservationDTO)){
      return null;
    }

    if(updateReservation.getStatus() == ReservationStatus.COMPLETED
        || updateReservation.getStatus() == ReservationStatus.CANCELLED){
      return null;
    }

    if(!updateReservation.getCheckIn().isAfter(LocalDate.now().plusDays(3))){
      return null;
    }

    updateReservation.setPetBusinessRoom(room);
    updateReservation.setSpecialRequests(reservationDTO.getSpecialRequests());
    updateReservation.setBusinessRequestInfo(reservationDTO.getBusinessRequestInfo());
    updateReservation.setCheckIn(reservationDTO.getCheckIn());
    updateReservation.setCheckOut(reservationDTO.getCheckOut());
    updateReservation.setPeriod(
        (int) ChronoUnit.DAYS.between(reservationDTO.getCheckIn(), reservationDTO.getCheckOut()));
    updateReservation.setUpdatedAt(LocalDateTime.now());
    updateReservation = reservationRepository.save(updateReservation);

    return reservationMapper.toBasicDTO(updateReservation);

  }

  //예약 취소(단 이미 결제까지 진행된 예약과, 아직 결제 진행이 아닌 예약은 별도로 처리)
  @Override
  @Transactional
  public ReservationDTO deleteReservation(ReservationDTO reservationDTO) {
    Reservation deleteReservation =
        reservationRepository.findById(reservationDTO.getId()).orElse(null);

    if(deleteReservation == null){
      return null;
    }

    if(deleteReservation.getStatus() == ReservationStatus.CANCELLED
    || deleteReservation.getStatus() == ReservationStatus.COMPLETED){
      return null;
    }

    // 결제 되기전 예약
    if(deleteReservation.getStatus() == ReservationStatus.PENDING){

      if(PetReservationService.deletePetReservation(reservationDTO.getId())){
        reservationRepository.deleteById(reservationDTO.getId());
      }else{
        throw new IllegalArgumentException("예약 삭제가 되지 않았습니다. 다시 시도해보세요!!");
      }

    }
    /*
    결제 된 후의 예약(CONFIRMED)
    결제 취소 -> 펫 예약 리스트 삭제 -> 예약 삭제
    */
    else{
      Payment payment= deleteReservation.getPayment();
      PaymentCancelRequestDTO cancelRequestDTO=new PaymentCancelRequestDTO();
      cancelRequestDTO.setImpUid(payment.getImpUid());
      cancelRequestDTO.setMerchantUid(payment.getMerchantUid());
      cancelRequestDTO.setAmount(payment.getAmount());
      cancelRequestDTO.setReason("예약 취소");
      PaymentDTO paymentDTO=paymentService.CancelPayment(cancelRequestDTO);
      if( paymentDTO == null ||
        !PetReservationService.deletePetReservation(reservationDTO.getId())){
        throw new IllegalArgumentException("예약 삭제가 되지 않았습니다. 다시 시도해보세요!!");
      }else{
        deleteReservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(deleteReservation);
      }

    }

    return reservationMapper.toBasicDTO(deleteReservation);
  }


  @Override
  public boolean UpdateValidation(ReservationDTO reservationDTO){
    Reservation existreservation = reservationRepository.findById(reservationDTO.getId()).orElse(null);
    try {

      if (isBlank(String.valueOf(reservationDTO.getCheckIn()))
          || isBlank(String.valueOf(reservationDTO.getCheckOut()))
          || (int) ChronoUnit.DAYS.between(reservationDTO.getCheckIn(),
          reservationDTO.getCheckOut()) <= 0) {
        return false;
      }
    PetBusinessRoom room = existreservation.getPetBusinessRoom();
      if(room !=null) {
        if (room.getId().equals(reservationDTO.getPetBusinessRoomId())
            && existreservation.getBusinessRequestInfo().equals(reservationDTO.getBusinessRequestInfo())
            && existreservation.getSpecialRequests().equals(reservationDTO.getSpecialRequests())
            && existreservation.getCheckIn().isEqual(reservationDTO.getCheckIn())
            && existreservation.getCheckOut().isEqual(reservationDTO.getCheckOut())) {
          return false;
        }
      }
      else{
        if (existreservation.getBusinessRequestInfo().equals(reservationDTO.getBusinessRequestInfo())
            && existreservation.getSpecialRequests().equals(reservationDTO.getSpecialRequests())
            && existreservation.getCheckIn().isEqual(reservationDTO.getCheckIn())
            && existreservation.getCheckOut().isEqual(reservationDTO.getCheckOut())) {
          return false;
        }
      }

      return true;

    }catch (Exception e){
      log.error(e.getMessage());
      return false;
    }
  }

  @Override
  public boolean isBlank(String str){
    return str == null || str.trim().isEmpty();
  }
}
