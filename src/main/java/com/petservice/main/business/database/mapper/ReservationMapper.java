package com.petservice.main.business.database.mapper;

import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.business.database.repository.PetBusinessRepository;
import com.petservice.main.business.database.repository.PetBusinessRoomRepository;
import com.petservice.main.payment.database.mapper.PaymentMapper;
import com.petservice.main.review.database.mapper.ReviewMapper;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationMapper {

  private final UserRepository userRepository;
  private final PetBusinessRepository petBusinessRepository;
  private final PetBusinessRoomRepository petBusinessRoomRepository;
  private final PaymentMapper paymentMapper;
  private final ReviewMapper reviewMapper;

  public Reservation toEntity(ReservationDTO reservationDTO){

    Reservation reservation= new Reservation();
    reservation.setId(reservationDTO.getId());
    reservation.setCheckIn(reservationDTO.getCheckIn());
    reservation.setCheckOut(reservationDTO.getCheckOut());
    reservation.setCreatedAt(reservationDTO.getCreatedAt());
    reservation.setUpdatedAt(reservationDTO.getUpdatedAt());
    reservation.setSpecialRequests(reservationDTO.getSpecialRequests());
    reservation.setBusinessRequestInfo(reservationDTO.getBusinessRequestInfo());
    reservation.setPeriod(reservationDTO.getPeriod());
    reservation.setStatus(reservationDTO.getStatus());

    if(reservationDTO.getUserId()!=null){
      reservation.setUser(userRepository.findById(reservationDTO.getUserId()).orElse(null));
    }else if(reservationDTO.getUserLoginId() != null){
      reservation.setUser(userRepository.findByUserLoginId(reservationDTO.getUserLoginId()).orElse(null));
    }
    if(reservationDTO.getPetBusinessId()!=null){
      reservation.setPetBusiness(petBusinessRepository.findById(reservationDTO.getPetBusinessId())
          .orElse(null));
    }else if(reservationDTO.getPetBusinessRegisterNumber()!=null){
      reservation.setPetBusiness(petBusinessRepository
          .findByRegistrationNumber(reservationDTO.getPetBusinessRegisterNumber()));
    }
    if(reservationDTO.getPetBusinessRoomId()!=null){
      reservation.setPetBusinessRoom(petBusinessRoomRepository
          .findById(reservationDTO.getPetBusinessRoomId()).orElse(null));
    }
    if(reservationDTO.getPaymentDTO()!=null){
      reservation.setPayment(paymentMapper.toEntity(reservationDTO.getPaymentDTO()));
    }
    if(reservationDTO.getReviewDTO()!=null){
      reservation.setReview(reviewMapper.toEntity(reservationDTO.getReviewDTO()));
    }
    return reservation;
  }

  public ReservationDTO toDTO(Reservation reservation){

    ReservationDTO reservationDTO= new ReservationDTO();
    reservationDTO.setId(reservation.getId());
    reservationDTO.setCheckIn(reservation.getCheckIn());
    reservationDTO.setCheckOut(reservation.getCheckOut());
    reservationDTO.setCreatedAt(reservation.getCreatedAt());
    reservationDTO.setUpdatedAt(reservation.getUpdatedAt());
    reservationDTO.setSpecialRequests(reservation.getSpecialRequests());
    reservationDTO.setBusinessRequestInfo(reservation.getBusinessRequestInfo());
    reservationDTO.setPeriod(reservation.getPeriod());
    reservationDTO.setStatus(reservation.getStatus());

    if(reservation.getUser()!=null){
      reservationDTO.setUserId(reservation.getUser().getId());
      reservationDTO.setUserLoginId(reservation.getUser().getUserLoginId());
    }
    if(reservation.getPetBusiness()!=null){
      reservationDTO.setPetBusinessId(reservation.getPetBusiness().getId());
      reservationDTO.setPetBusinessRegisterNumber(reservation.getPetBusiness().getRegistrationNumber());
      reservationDTO.setPetBusinessName(reservation.getPetBusiness().getBusinessName());
    }
    if(reservation.getPetBusinessRoom()!=null){
      reservationDTO.setPetBusinessRoomId(reservation.getPetBusinessRoom().getId());
      reservationDTO.setRoomType(reservation.getPetBusinessRoom().getRoomType());
    }
    if(reservation.getPayment()!=null){
      reservationDTO.setPaymentDTO(paymentMapper.toDTO(reservation.getPayment()));
    }
    if(reservation.getReview()!=null){
      reservationDTO.setReviewDTO(reviewMapper.toDTO(reservation.getReview()));
    }
    return reservationDTO;
  }

  public ReservationDTO toBasicDTO(Reservation reservation){

    ReservationDTO reservationDTO= new ReservationDTO();
    reservationDTO.setId(reservation.getId());
    reservationDTO.setCheckIn(reservation.getCheckIn());
    reservationDTO.setCheckOut(reservation.getCheckOut());
    reservationDTO.setCreatedAt(reservation.getCreatedAt());
    reservationDTO.setUpdatedAt(reservation.getUpdatedAt());
    reservationDTO.setSpecialRequests(reservation.getSpecialRequests());
    reservationDTO.setBusinessRequestInfo(reservation.getBusinessRequestInfo());
    reservationDTO.setPeriod(reservation.getPeriod());
    reservationDTO.setStatus(reservation.getStatus());

    if(reservation.getPayment()!=null){
      reservationDTO.setPaymentDTO(paymentMapper.toDTO(reservation.getPayment()));
    }
    if(reservation.getReview()!=null){
      reservationDTO.setReviewDTO(reviewMapper.toDTO(reservation.getReview()));
    }
    return reservationDTO;
  }


}
