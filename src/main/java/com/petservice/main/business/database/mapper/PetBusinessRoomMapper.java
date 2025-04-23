package com.petservice.main.business.database.mapper;

import com.petservice.main.business.database.dto.PetBusinessRoomDTO;
import com.petservice.main.business.database.entity.PetBusinessRoom;
import com.petservice.main.business.database.repository.PetBusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetBusinessRoomMapper {

  private final PetBusinessRepository petBusinessRepository;

  private final ReservationMapper reservationMapper;

  public PetBusinessRoom toEntity(PetBusinessRoomDTO petBusinessRoomDTO){

    PetBusinessRoom petBusinessRoom=new PetBusinessRoom();
    petBusinessRoom.setId(petBusinessRoomDTO.getId());
    petBusinessRoom.setRoomType(petBusinessRoomDTO.getRoomType());
    petBusinessRoom.setDescription(petBusinessRoomDTO.getDescription());
    petBusinessRoom.setRoomCount(petBusinessRoomDTO.getRoomCount());

    if(petBusinessRoomDTO.getPetBusinessId()!=null){
      petBusinessRoom.setPetBusiness(petBusinessRepository
          .findById(petBusinessRoomDTO.getPetBusinessId()).orElse(null));
    }
    if(petBusinessRoomDTO.getReservationDTOList()!=null){
      petBusinessRoom.setReservationList(petBusinessRoomDTO.getReservationDTOList().stream()
          .map(reservationMapper::toEntity).toList());
    }
    return petBusinessRoom;
  }

  public PetBusinessRoomDTO toDTO(PetBusinessRoom petBusinessRoom){

    PetBusinessRoomDTO petbusinessRoomDTO=new PetBusinessRoomDTO();
    petbusinessRoomDTO.setId(petBusinessRoom.getId());
    petbusinessRoomDTO.setRoomType(petBusinessRoom.getRoomType());
    petbusinessRoomDTO.setDescription(petBusinessRoom.getDescription());
    petbusinessRoomDTO.setRoomCount(petBusinessRoom.getRoomCount());
    if(petBusinessRoom.getPetBusiness()!=null){
      petbusinessRoomDTO.setPetBusinessId(petBusinessRoom.getPetBusiness().getId());
    }
    if(petBusinessRoom.getReservationList()!=null){
      petbusinessRoomDTO.setReservationDTOList(petBusinessRoom.getReservationList().stream()
          .map(reservationMapper::toDTO).toList());
    }
    return petbusinessRoomDTO;
  }

  public PetBusinessRoomDTO toBasicDTO(PetBusinessRoom petBusinessRoom){

    PetBusinessRoomDTO petbusinessRoomDTO=new PetBusinessRoomDTO();
    petbusinessRoomDTO.setId(petBusinessRoom.getId());
    petbusinessRoomDTO.setRoomType(petBusinessRoom.getRoomType());
    petbusinessRoomDTO.setDescription(petBusinessRoom.getDescription());
    petbusinessRoomDTO.setRoomCount(petBusinessRoom.getRoomCount());

    return petbusinessRoomDTO;
  }
}
