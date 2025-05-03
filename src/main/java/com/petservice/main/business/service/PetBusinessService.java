package com.petservice.main.business.service;

import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.dto.PetBusinessRoomDTO;
import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.mapper.PetBusinessMapper;
import com.petservice.main.business.database.repository.PetBusinessRepository;
import com.petservice.main.business.service.Interface.PetBusinessServiceInterface;
import com.petservice.main.business.service.Interface.PetBusinessTypeServiceInterface;
import com.petservice.main.user.database.dto.AddressDTO;
import com.petservice.main.user.service.Interface.AddressServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetBusinessService implements PetBusinessServiceInterface {

  private final PetBusinessTypeServiceInterface petBusinessTypeService;
  private final PetBusinessRepository petBusinessRepository;

  private final AddressServiceInterface addressService;
  private final PetBusinessMapper petBusinessMapper;

  @Override
  public PetBusinessDTO getBusinessDto(Long business_id) {
    return null;
  }

  /*
    서비스 내에 등록된 사업자만 포함
    회원의 근처에 있는 실제 사업자만 검색(is_around)('시' 기준(내부 db)),
    다양한 검색 옵션(사업자 명, 섹터 코드(KSIC 분류코드), 타입 코드(색인어), 주소)
  */
  @Override
  @Transactional(readOnly = true)
  public Page<PetBusinessDTO> getBusinessList(String businessName, String sectorCode, String typeCode,
      String userLoginId, boolean is_around, int page, int size) {

    Pageable pageable = PageRequest.of(page, size);
    Page<PetBusiness> petBusinessPage = null;

    if (is_around) {
      log.info("city(시)를 기준으로 주변을 탐색합니다.");
      AddressDTO userAddress=addressService.getAddressByUserLoginId(userLoginId);
      petBusinessPage=petBusinessRepository.findServiceAndAround(
        businessName,sectorCode,typeCode,userAddress.getCity(),pageable);
    }else{
      petBusinessPage=petBusinessRepository.findServiceAll(
        businessName,sectorCode,typeCode,pageable);
    }

    return petBusinessPage.map(petBusinessMapper::toBasicDTO);
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
