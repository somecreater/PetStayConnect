package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.PetBusinessRoomDTO;

import java.util.List;

public interface PetBusinessRoomServiceInterface {

  public List<PetBusinessRoomDTO> getRoomByBusinessRegisterNumber(String RegisterNumber);
  public List<PetBusinessRoomDTO> getRoomByBusinessId(Long business_id);
}
