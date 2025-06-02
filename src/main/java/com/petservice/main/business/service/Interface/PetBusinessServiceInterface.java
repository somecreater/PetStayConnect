package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.dto.PetBusinessRoomDTO;
import com.petservice.main.business.database.dto.PetBusinessTagDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PetBusinessServiceInterface {

  /*
    서비스 내에 등록된 사업자만 포함(일부 민감한 정보 null 값 처리),
    회원의 근처에 있는 실제 사업자만 검색(is_around), 다양한 검색 옵션(사업자 명, 섹터 코드(KSIC 분류코드), 타입 코드(색인어), 주소)
    데이터를 전부다 보여주기에는 성능상 제약사항이 생기므로 특별한 방법이 필요하다. (ex,pageable을 이용해서 일부만 보여주든가 등등)
  */
  public Page<PetBusinessDTO> getBusinessList(String businessName, String sectorCode, String typeCode,
      String userLoginId, boolean is_around, int page, int size);
  public Page<PetBusinessDTO> getBusinessListByTag(PetBusinessTagDTO tagDTO, int page, int size);
  public PetBusinessDTO getBusinessDto(Long business_id);
  public PetBusinessDTO getBusinessDtoByUserLoginId(String userLoginId);
  public PetBusinessDTO getBusinessDtoBYUserId(Long User_id);
  public PetBusinessDTO registerBusiness(PetBusinessDTO petBusinessDTO);
  public PetBusinessDTO updateBusiness(PetBusinessDTO petBusinessDTO);
  public boolean deleteBusiness(Long business_id);
  public boolean deleteBusinessByUser(Long user_id);

  //실제로 존재하는 사업자인지 확인
  public boolean BusinessValidation(PetBusinessDTO petBusinessDTO);
  public boolean existBusiness(PetBusinessDTO petBusinessDTO);

  //값 검증
  public boolean insertValidation(PetBusinessDTO petBusinessDTO);
  public boolean updateValidation(PetBusinessDTO petBusinessDTO);
  public boolean isBlank(String str);

}
