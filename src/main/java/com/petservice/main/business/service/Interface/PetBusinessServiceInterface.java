package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.dto.PetBusinessRoomDTO;
import com.petservice.main.user.database.dto.AddressDTO;

import java.util.List;

public interface PetBusinessServiceInterface {

  /*
    서비스 내에 등록된 사업자만 포함 or MAP API를 통해 검색된 실제 사업자도 포함(is_service),
    회원의 근처에 있는 실제 사업자만 검색(is_around), 다양한 검색 옵션(사업자 명, 사업 타입, 주소)
  */
  public List<PetBusinessDTO> getBusinessList(String business_name, String type_name,
      AddressDTO addressDTO, boolean is_service, boolean is_around);
  public PetBusinessDTO getBusinessDto(Long business_id);
  public PetBusinessDTO registerBusiness(PetBusinessDTO petBusinessDTO);
  public PetBusinessDTO updateBusiness(PetBusinessDTO petBusinessDTO);
  public boolean deleteBusiness(Long business_id);

  public List<PetBusinessRoomDTO> getRoomList(Long business_id);
  public PetBusinessRoomDTO getRoom(Long room_id);
  public PetBusinessRoomDTO registerRoom(Long business_id, PetBusinessRoomDTO petBusinessRoomDTO);
  public PetBusinessRoomDTO updateRoom(Long business_id, PetBusinessRoomDTO petBusinessRoomDTO);
  public boolean deleteRoom(Long business_id, Long room_id);

  //실제로 존재하는 사업자인지 확인
  public boolean BusinessValidation(PetBusinessDTO petBusinessDTO);
  public boolean existBusiness(PetBusinessDTO petBusinessDTO);
}
