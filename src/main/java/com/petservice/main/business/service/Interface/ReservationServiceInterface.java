package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.business.database.dto.ReservationRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReservationServiceInterface {

  public Page<ReservationDTO> getReservationList(String user_login_id, Pageable pageable);
  public Page<ReservationDTO> getReservationListByBusiness(Long Business_id, Pageable pageable);
  public ReservationDTO getReservation(String user_login_id, Long ReservationId);
  public ReservationDTO getReservationByBusiness(String RegisterNumber, Long ReservationId);
  public ReservationDTO getReservationById(Long ReservationId);

  public ReservationDTO registerReservation(ReservationRequest reservationRequest);
  public ReservationDTO updateReservation(ReservationDTO reservationDTO);
  public ReservationDTO deleteReservation(ReservationDTO reservationDTO);
  public boolean deleteReservationByUserId(Long id);
  public boolean deleteReservationByBusinessId(Long id);
  public boolean updateDeleteUser(Long user_id);
  public boolean updateDeleteBusiness(Long business_id);

  public boolean UpdateValidation(ReservationDTO reservationDTO);
  public boolean isBlank(String str);
}
