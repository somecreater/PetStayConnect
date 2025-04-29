package com.petservice.main.business.service;

import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.dto.PetBusinessRoomDTO;
import com.petservice.main.business.service.Interface.PetBusinessServiceInterface;
import com.petservice.main.user.database.dto.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PetBusinessService implements PetBusinessServiceInterface {

  @Override
  public PetBusinessDTO getBusinessDto(Long business_id) {
    return null;
  }

  @Override
  public List<PetBusinessDTO> getBusinessList(String business_name, String type_name,
      AddressDTO addressDTO, boolean is_service, boolean is_around) {
    return List.of();
  }

  @Override
  public PetBusinessDTO registerBusiness(PetBusinessDTO petBusinessDTO) {
    return null;
  }

  @Override
  public PetBusinessDTO updateBusiness(PetBusinessDTO petBusinessDTO) {
    return null;
  }

  @Override
  public boolean deleteBusiness(Long business_id) {
    return false;
  }

  @Override
  public PetBusinessRoomDTO getRoom(Long room_id) {
    return null;
  }

  @Override
  public List<PetBusinessRoomDTO> getRoomList(Long business_id) {
    return List.of();
  }

  @Override
  public PetBusinessRoomDTO registerRoom(Long business_id, PetBusinessRoomDTO petBusinessRoomDTO) {
    return null;
  }

  @Override
  public PetBusinessRoomDTO updateRoom(Long business_id, PetBusinessRoomDTO petBusinessRoomDTO) {
    return null;
  }

  @Override
  public boolean deleteRoom(Long business_id, Long room_id) {
    return false;
  }

  @Override
  public boolean BusinessValidation(PetBusinessDTO petBusinessDTO) {
    boolean result= true;

    return result;
  }

  @Override
  public boolean existBusiness(PetBusinessDTO petBusinessDTO) {
    boolean result= false;

    return result;
  }
}
