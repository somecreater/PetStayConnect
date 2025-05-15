package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.PetBusinessRoomDTO;

import java.util.List;

public interface PetBusinessRoomServiceInterface {

  public List<PetBusinessRoomDTO> getRoomByBusinessRegisterNumber(String RegisterNumber);
  public List<PetBusinessRoomDTO> getRoomByBusinessId(Long business_id);
  public PetBusinessRoomDTO getRoom(Long business_id, Long room_id);
  public PetBusinessRoomDTO createRoom(Long business_id, PetBusinessRoomDTO petBusinessRoomDTO);
  public PetBusinessRoomDTO updateRoom(Long business_id, Long room_id, PetBusinessRoomDTO petBusinessRoomDTO);
  public boolean deleteRoom(Long business_id, Long room_id);
  public boolean validationRoom(PetBusinessRoomDTO petBusinessRoomDTO);
  public boolean isBlank(String str);
}
