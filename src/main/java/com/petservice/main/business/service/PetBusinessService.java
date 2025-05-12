package com.petservice.main.business.service;

import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.dto.PetBusinessRoomDTO;
import com.petservice.main.business.database.entity.BusinessStatus;
import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.entity.Varification;
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

import java.time.LocalDateTime;
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
  @Transactional(readOnly = true)
  public PetBusinessDTO getBusinessDto(Long business_id) {
    PetBusiness petBusiness=petBusinessRepository.findById(business_id).orElse(null);
    if(petBusiness ==null){
      return null;
    }
    PetBusinessDTO petBusinessDTO=petBusinessMapper.toBasicDTO(petBusiness);
    petBusinessDTO.setBankAccount(null);
    return petBusinessDTO;
  }

  @Override
  @Transactional(readOnly = true)
  public PetBusinessDTO getBusinessDtoByUserLoginId(String userLoginId){
    PetBusiness petBusiness= petBusinessRepository.findByUser_UserLoginId(userLoginId);

    if(petBusiness == null){
      return null;
    }
    PetBusinessDTO petBusinessDTO=petBusinessMapper.toBasicDTO(petBusiness);
    petBusinessDTO.setBankAccount(null);
    return petBusinessDTO;
  }

  @Override
  @Transactional(readOnly = true)
  public PetBusinessDTO getBusinessDtoBYUserId(Long User_id){
    PetBusiness petBusiness = petBusinessRepository.findByUser_Id(User_id);

    if(petBusiness !=null){
      return null;
    }
    PetBusinessDTO petBusinessDTO=petBusinessMapper.toBasicDTO(petBusiness);
    petBusinessDTO.setBankAccount(null);
    return petBusinessDTO;
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

    return petBusinessPage.map(business -> {
      business.setBankAccount(null);
      return petBusinessMapper.toBasicDTO(business);
    });
  }

  @Override
  @Transactional
  public PetBusinessDTO registerBusiness(PetBusinessDTO petBusinessDTO) {

    if (petBusinessRepository.existsByRegistrationNumber(petBusinessDTO.getRegistrationNumber())
      || insertValidation(petBusinessDTO)) {
      return null;
    }

    petBusinessDTO.setStatus(BusinessStatus.OPERATION);
    petBusinessDTO.setAvgRate(null);
    petBusinessDTO.setVarification(Varification.NONE);

    PetBusiness petBusiness =
        petBusinessRepository.save(petBusinessMapper.toEntity(petBusinessDTO));

    return petBusinessMapper.toBasicDTO(petBusiness);

  }

  @Override
  @Transactional
  public PetBusinessDTO updateBusiness(PetBusinessDTO petBusinessDTO) {
    PetBusiness exPetBusiness=petBusinessRepository.findById(petBusinessDTO.getId()).orElse(null);
    if(exPetBusiness == null || updateValidation(petBusinessDTO)){
      return null;
    }

    exPetBusiness.setStatus(petBusinessDTO.getStatus());
    exPetBusiness.setMinPrice(petBusinessDTO.getMinPrice());
    exPetBusiness.setMaxPrice(petBusinessDTO.getMaxPrice());
    exPetBusiness.setFacilities(petBusinessDTO.getFacilities());
    exPetBusiness.setDescription(petBusinessDTO.getDescription());
    exPetBusiness.setBankAccount(petBusinessDTO.getBankAccount());
    exPetBusiness.setProvince(petBusinessDTO.getProvince());
    exPetBusiness.setCity(petBusinessDTO.getCity());
    exPetBusiness.setTown(petBusinessDTO.getTown());
    exPetBusiness.setUpdatedAt(LocalDateTime.now());

    PetBusiness updatePetBusiness=petBusinessRepository.save(exPetBusiness);

    return petBusinessMapper.toBasicDTO(updatePetBusiness);
  }

  // 추후 수정
  @Override
  public boolean deleteBusiness(Long business_id) {
    try {
      petBusinessRepository.deleteById(business_id);
      return true;
    }catch (Exception e){
      e.printStackTrace();
      log.error("사업자 데이터 삭제 오류: {}", e.getMessage());
      throw new RuntimeException(e);
    }
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


  @Override
  public boolean insertValidation(PetBusinessDTO petBusinessDTO){
    if(petBusinessDTO == null){
      return false;
    }
    if (isBlank(petBusinessDTO.getBusinessName())
        || isBlank(petBusinessDTO.getPetBusinessTypeName())
        || isBlank(petBusinessDTO.getProvince())
        || isBlank(petBusinessDTO.getCity())
        || isBlank(petBusinessDTO.getTown())) {
      return false;
    }

    if (petBusinessDTO.getStatus() == null
        || petBusinessDTO.getVarification() == null) {
      return false;
    }

    if (petBusinessDTO.getMinPrice() == null
        || petBusinessDTO.getMaxPrice() == null
        || petBusinessDTO.getMinPrice() > petBusinessDTO.getMaxPrice()) {
      return false;
    }

    String regNum = petBusinessDTO.getRegistrationNumber();
    if (isBlank(regNum)) {
      return false;
    }

    return true;
  }

  @Override
  @Transactional(readOnly = true)
  public boolean updateValidation(PetBusinessDTO petBusinessDTO){

    if(petBusinessDTO == null){
      return false;
    }

    if ( isBlank(Long.toString(petBusinessDTO.getId()))
        || isBlank(petBusinessDTO.getBusinessName())
        || isBlank(petBusinessDTO.getStatus().name())
        || isBlank(petBusinessDTO.getRegistrationNumber())
        || isBlank(petBusinessDTO.getVarification().name())
        || isBlank(Integer.toString(petBusinessDTO.getMaxPrice()))
        || isBlank(Integer.toString(petBusinessDTO.getMinPrice()))
        || isBlank(petBusinessDTO.getStatus().name())
        || isBlank(petBusinessDTO.getVarification().name())
        || isBlank(petBusinessDTO.getProvince())
        || isBlank(petBusinessDTO.getCity())
        || isBlank(petBusinessDTO.getTown())
        || isBlank(petBusinessDTO.getPetBusinessTypeName())){
      return false;
    }

    PetBusiness petBusiness=petBusinessRepository.findById(petBusinessDTO.getId()).orElse(null);

    if(petBusiness == null){
      return false;
    }

    if(petBusinessDTO.getStatus().equals(petBusiness.getStatus())
      && petBusinessDTO.getMinPrice().compareTo(petBusiness.getMinPrice())==0
      && petBusinessDTO.getMaxPrice().compareTo(petBusiness.getMaxPrice())==0
      && petBusinessDTO.getFacilities().equals(petBusiness.getFacilities())
      && petBusinessDTO.getDescription().equals(petBusiness.getDescription())
      && petBusinessDTO.getBankAccount().equals(petBusiness.getBankAccount())
      && petBusinessDTO.getProvince().equals(petBusiness.getProvince())
      && petBusinessDTO.getCity().equals(petBusiness.getCity())
      && petBusinessDTO.getTown().equals(petBusiness.getTown())){

      return false;
    }

    return true;
  }

  @Override
  public boolean isBlank(String str){
    return str == null || str.trim().isEmpty();
  }
}
