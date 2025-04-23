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
  private final PetReservationMapper petReservationMapper;
  private final ReviewMapper reviewMapper;
  private final PetBusinessRoomMapper petBusinessRoomMapper;

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
    }
    if(reservationDTO.getPetBusinessId()!=null){
      reservation.setPetBusiness(petBusinessRepository.findById(reservationDTO.getPetBusinessId())
          .orElse(null));
    }
    if(reservationDTO.getPetBusinessRoomId()!=null){
      reservation.setPetBusinessRoom(petBusinessRoomRepository
          .findById(reservationDTO.getPetBusinessRoomId()).orElse(null));
    }
    if(reservationDTO.getPaymentDTO()!=null){
      reservation.setPayment(paymentMapper.toEntity(reservationDTO.getPaymentDTO()));
    }
    if(reservationDTO.getPetReservationDTOList()!=null){
      reservation.setPetReservationList(reservationDTO.getPetReservationDTOList().stream()
          .map(petReservationMapper::toEntity).toList());
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
    }
    if(reservation.getPetBusiness()!=null){
      reservationDTO.setPetBusinessId(reservation.getPetBusiness().getId());
      reservationDTO.setPetBusinessName(reservation.getPetBusiness().getBusinessName());
    }
    if(reservation.getPetBusinessRoom()!=null){
      reservationDTO.setPetBusinessRoomId(reservation.getPetBusinessRoom().getId());
    }
    if(reservation.getPayment()!=null){
      reservationDTO.setPaymentDTO(paymentMapper.toDTO(reservation.getPayment()));
    }
    if(reservation.getPetReservationList()!=null){
      reservationDTO.setPetReservationDTOList(reservation.getPetReservationList().stream()
          .map(petReservationMapper::toDTO).toList());
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
    if(reservation.getPetReservationList()!=null){
      reservationDTO.setPetReservationDTOList(reservation.getPetReservationList().stream()
          .map(petReservationMapper::toDTO).toList());
    }
    if(reservation.getReview()!=null){
      reservationDTO.setReviewDTO(reviewMapper.toDTO(reservation.getReview()));
    }
    return reservationDTO;
  }


}
