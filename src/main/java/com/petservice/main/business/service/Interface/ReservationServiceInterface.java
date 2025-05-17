package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.business.database.dto.ReservationRequest;

import java.util.List;

public interface ReservationServiceInterface {

  public List<ReservationDTO> getReservationList(String user_login_id);
  public List<ReservationDTO> getReservationListByBusiness(Long Business_id);
  public ReservationDTO getReservation(String user_login_id, Long ReservationId);
  public ReservationDTO getReservationByBusiness(String RegisterNumber, Long ReservationId);
  public ReservationDTO getReservationById(Long ReservationId);

  public ReservationDTO registerReservation(ReservationRequest reservationRequest);
  public ReservationDTO updateReservation(ReservationDTO reservationDTO);
  public ReservationDTO deleteReservation(ReservationDTO reservationDTO);

  public boolean UpdateValidation(ReservationDTO reservationDTO);
  public boolean isBlank(String str);
}
