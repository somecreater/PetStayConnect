package com.petservice.main.business.database.mapper;

import com.petservice.main.business.database.dto.PetReservationDTO;
import com.petservice.main.business.database.entity.PetReservation;
import com.petservice.main.business.database.repository.ReservationRepository;
import com.petservice.main.user.database.mapper.PetMapper;
import com.petservice.main.user.database.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetReservationMapper{

  private final PetRepository petRepository;
  private final ReservationRepository reservationRepository;

  private final PetMapper petMapper;
  private final ReservationMapper reservationMapper;
  public PetReservation toEntity(PetReservationDTO petReservationDTO){

    PetReservation petReservation=new PetReservation();
    petReservation.setId(petReservation.getId());
    if(petReservationDTO.getPetId()!=null){
      petReservation.setPet(petRepository.findById(petReservationDTO.getPetId()).orElse(null));
    }
    if(petReservationDTO.getReservationId()!=null){
      petReservation.setReservation(reservationRepository
          .findById(petReservationDTO.getReservationId()).orElse(null));
    }
    return petReservation;
  }

  public PetReservationDTO toDTO(PetReservation petReservation){

    PetReservationDTO petReservationDTO=new PetReservationDTO();
    petReservationDTO.setId(petReservation.getId());
    if(petReservation.getPet()!=null){
      petReservationDTO.setPetDTO(petMapper.toBasicDTO(petReservation.getPet()));
      petReservationDTO.setPetId(petReservation.getPet().getId());
    }
    if(petReservation.getReservation()!=null){
      petReservationDTO.setReservationDTO(reservationMapper.toBasicDTO(
          petReservation.getReservation()));
      petReservationDTO.setReservationId(petReservation.getReservation().getId());
    }
    return petReservationDTO;
  }

  public PetReservationDTO toBasicDTO(PetReservation petReservation){

    PetReservationDTO petReservationDTO=new PetReservationDTO();
    petReservationDTO.setId(petReservation.getId());
    return petReservationDTO;
  }
}
