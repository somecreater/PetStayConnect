package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.NaverPlaceItem;
import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.dto.PetBusinessRoomDTO;
import com.petservice.main.business.database.dto.PetBusinessTypeDTO;
import com.petservice.main.user.database.dto.AddressDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PetBusinessServiceInterface {

  /*
    서비스 내에 등록된 사업자만 포함 or MAP API를 통해 검색된 실제 사업자도 포함(is_service),
    회원의 근처에 있는 실제 사업자만 검색(is_around), 다양한 검색 옵션(사업자 명, 섹터 코드(KSIC 분류코드), 타입 코드(색인어), 주소)
    데이터를 전부다 보여주기에는 성능상 제약사항이 생기므로 특별한 방법이 필요하다. (ex,pageable을 이용해서 일부만 보여주든가 등등)
  */
  public Page<PetBusinessDTO> getBusinessList(String businessName, String sectorCode, String typeCode,
      String userLoginId, boolean is_around, int page, int size);
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
