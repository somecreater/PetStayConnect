package com.petservice.main.business.service;

import com.petservice.main.business.database.dto.PetReservationDTO;
import com.petservice.main.business.database.dto.ReservationDTO;
import com.petservice.main.business.database.entity.PetReservation;
import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.business.database.mapper.PetReservationMapper;
import com.petservice.main.business.database.repository.PetReservationRepository;
import com.petservice.main.business.database.repository.ReservationRepository;
import com.petservice.main.business.service.Interface.PetReservationServiceInterface;
import com.petservice.main.user.database.dto.PetDTO;
import com.petservice.main.user.database.entity.Pet;
import com.petservice.main.user.database.mapper.PetMapper;
import com.petservice.main.user.database.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetReservationService implements PetReservationServiceInterface {

  private final PetReservationMapper petReservationMapper;
  private final PetMapper petMapper;
  private final PetReservationRepository PetReservationRepository;

  private final PetRepository petRepository;
  private final ReservationRepository reservationRepository;

  @Override
  @Transactional(readOnly = true)
  public List<PetDTO> getPetListByReservation(Long reservation_id) {
    List<PetReservation> petReservations =
        PetReservationRepository.findByReservation_Id(reservation_id);
    if(petReservations == null){
      return null;
    }

    List<Pet> pets = petReservations.stream().map(PetReservation::getPet).toList();
    return pets.stream().map(petMapper::toBasicDTO).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PetReservationDTO> getPetReservationListByReservation(Long reservation_id) {
    return PetReservationRepository.findByReservation_Id(reservation_id).stream()
        .map(petReservationMapper::toDTO).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PetReservationDTO> getPetReservationListByPet(Long pet_id) {
    return PetReservationRepository.findByPet_Id(pet_id).stream()
        .map(petReservationMapper::toDTO).toList();
  }

  @Override
  @Transactional
  public PetReservationDTO registerPetReservation(PetDTO petDTO, ReservationDTO reservationDTO) {
    try {
      if (petDTO == null || reservationDTO == null) {
        throw new IllegalArgumentException("입력된 값이 잘못되었습니다.");
      }
      PetReservation petReservation = new PetReservation();
      Pet pet=petRepository.findById(petDTO.getId()).orElse(null);
      Reservation reservation=reservationRepository.findById(reservationDTO.getId()).orElse(null);
      if(pet == null || reservation == null){
        return null;
      }

      petReservation.setPet(pet);
      petReservation.setReservation(reservation);
      PetReservation newPetReservation=PetReservationRepository.save(petReservation);
      return petReservationMapper.toDTO(newPetReservation);

    }catch (Exception e){
      throw new IllegalArgumentException("입력된 값이 잘못되었습니다.");
    }
  }

  @Override
  @Transactional
  public boolean deletePetReservation(Long reservation_id){
    try{
      int delete_count=PetReservationRepository.deleteByReservationId(reservation_id);
      if(delete_count >= 0) {
        return true;
      }else{
        return false;
      }
    }catch (Exception e){
      log.error("얘기치 못한 오류 발생!!! {}",e.getMessage());
      return false;
    }
  }

  @Override
  @Transactional
  public boolean deletePetReservationByUserId(Long id){
    try{
      long delete_count= PetReservationRepository.deleteByReservation_User_Id(id);
      if(delete_count >= 0){
        log.info("숫자: {}",delete_count);
        return true;
      }
      return false;
    }catch(Exception e){
      log.error("얘기치 못한 오류 발생!!! {}",e.getMessage());
      return false;
    }
  }

  @Override
  @Transactional
  public boolean deletePetReservationByBusinessId(Long id){
    try{
      long delete_count= PetReservationRepository.deleteByReservation_PetBusiness_Id(id);
      if(delete_count >= 0){
        log.info("숫자: {}",delete_count);
        return true;
      }
      return false;
    }catch(Exception e){
      log.error("얘기치 못한 오류 발생!!! {}",e.getMessage());
      return false;
    }
  }

}
