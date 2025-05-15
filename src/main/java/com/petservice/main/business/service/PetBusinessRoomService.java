package com.petservice.main.business.service;

import com.petservice.main.business.database.dto.PetBusinessRoomDTO;
import com.petservice.main.business.database.entity.PetBusinessRoom;
import com.petservice.main.business.database.mapper.PetBusinessRoomMapper;
import com.petservice.main.business.database.repository.PetBusinessRoomRepository;
import com.petservice.main.business.database.repository.ReservationRepository;
import com.petservice.main.business.service.Interface.PetBusinessRoomServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetBusinessRoomService implements PetBusinessRoomServiceInterface {

  private final PetBusinessRoomRepository petBusinessRoomRepository;
  private final PetBusinessRoomMapper petBusinessRoomMapper;
  private final ReservationRepository reservationRepository;

  @Override
  @Transactional(readOnly = true)
  public List<PetBusinessRoomDTO> getRoomByBusinessRegisterNumber(String RegisterNumber) {
    return petBusinessRoomRepository.findByPetBusiness_RegistrationNumber(RegisterNumber)
        .stream().map(petBusinessRoomMapper::toBasicDTO).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<PetBusinessRoomDTO> getRoomByBusinessId(Long business_id) {
    return petBusinessRoomRepository.findByPetBusiness_Id(business_id)
        .stream().map(petBusinessRoomMapper::toBasicDTO).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public PetBusinessRoomDTO getRoom(Long business_id, Long room_id){
    PetBusinessRoom room= petBusinessRoomRepository.findById(room_id).orElse(null);

    if(room == null){
      return null;
    }
    if(!Objects.equals(room.getPetBusiness().getId(), business_id)){
      return null;
    }
    return petBusinessRoomMapper.toBasicDTO(room);
  }

  @Override
  @Transactional
  public PetBusinessRoomDTO createRoom(Long business_id, PetBusinessRoomDTO petBusinessRoomDTO) {
    if(petBusinessRoomRepository.existsByRoomTypeAndPetBusiness_Id(
        petBusinessRoomDTO.getRoomType(),
        business_id
    )){
      return null;
    }

    if(!validationRoom(petBusinessRoomDTO)){
      return null;
    }

    PetBusinessRoom insertRoom= petBusinessRoomRepository.save(
        petBusinessRoomMapper.toEntity(petBusinessRoomDTO));
    return petBusinessRoomMapper.toBasicDTO(insertRoom);
  }

  @Override
  @Transactional
  public PetBusinessRoomDTO updateRoom(Long business_id, Long room_id, PetBusinessRoomDTO petBusinessRoomDTO) {
    PetBusinessRoom exRoom= petBusinessRoomRepository.findByIdAndPetBusiness_Id(room_id, business_id);
    if(exRoom == null){
      return null;
    }
    exRoom.setRoomCount(petBusinessRoomDTO.getRoomCount());
    exRoom.setRoomType(petBusinessRoomDTO.getRoomType());
    exRoom.setDescription(petBusinessRoomDTO.getDescription());

    PetBusinessRoom updateRoom= petBusinessRoomRepository.save(exRoom);

    return petBusinessRoomMapper.toBasicDTO(updateRoom);
  }

  @Override
  @Transactional
  public boolean deleteRoom(Long business_id, Long room_id) {
    log.info("{}  {}", business_id, room_id);
    try {
      if (reservationRepository.existsByPetBusinessRoom_IdAndPetBusiness_Id(room_id, business_id)) {
        return false;
      }
      petBusinessRoomRepository.deleteById(room_id);
      return true;
    }catch (Exception e){
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean validationRoom(PetBusinessRoomDTO petBusinessRoomDTO){

    if(petBusinessRoomDTO == null){
      return false;
    }

    if(isBlank(petBusinessRoomDTO.getRoomType())
    || isBlank(petBusinessRoomDTO.getPetBusinessRegisterNumber())
    || isBlank(petBusinessRoomDTO.getDescription())) {
      return false;
    }
    if(petBusinessRoomDTO.getPetBusinessId() == null
    || petBusinessRoomDTO.getRoomCount() == null){
      return false;
    }

    return true;
  }

  @Override
  public boolean isBlank(String str){
    return str == null || str.trim().isEmpty();
  }
}
