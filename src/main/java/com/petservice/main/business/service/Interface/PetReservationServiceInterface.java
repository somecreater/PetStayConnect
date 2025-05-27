package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.PetReservationDTO;
import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.user.database.dto.PetDTO;

import java.util.List;

public interface PetReservationServiceInterface {

  public List<PetDTO> getPetListByReservation(Long reservation_id);
  public List<PetReservationDTO> getPetReservationListByReservation(Long reservation_id);
  public List<PetReservationDTO> getPetReservationListByPet(Long pet_id);

  public PetReservationDTO registerPetReservation(PetDTO petDTO, ReservationDTO reservationDTO);
  public boolean deletePetReservation(Long reservation_id);
  public boolean deletePetReservationByUserId(Long id);
  public boolean deletePetReservationByBusinessId(Long id);
}
