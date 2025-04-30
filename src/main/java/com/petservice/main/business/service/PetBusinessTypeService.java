package com.petservice.main.business.service;

import com.petservice.main.business.database.dto.PetBusinessTypeDTO;
import com.petservice.main.business.database.entity.PetBusinessType;
import com.petservice.main.business.database.mapper.PetBusinessTypeMapper;
import com.petservice.main.business.database.repository.PetBusinessRepository;
import com.petservice.main.business.database.repository.PetBusinessTypeRepository;
import com.petservice.main.business.service.Interface.PetBusinessTypeServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class PetBusinessTypeService implements PetBusinessTypeServiceInterface {

  private final PetBusinessTypeRepository petBusinessTypeRepository;
  private final PetBusinessRepository petBusinessRepository;

  private final PetBusinessTypeMapper petBusinessTypeMapper;

  @Override
  @Transactional
  public PetBusinessTypeDTO registerType(PetBusinessTypeDTO petBusinessTypeDTO) {
    try {

      PetBusinessType petBusinessType = new PetBusinessType();

      if(!insertValidationType(petBusinessTypeDTO)){
        return null;
      }

      petBusinessType.setTypeName(petBusinessTypeDTO.getTypeName());
      petBusinessType.setDescription(petBusinessTypeDTO.getDescription());
      petBusinessType.setSectorCode(petBusinessTypeDTO.getSectorCode());
      petBusinessType.setTypeCode(petBusinessTypeDTO.getTypeCode());
      return petBusinessTypeMapper.toBasicDTO(petBusinessTypeRepository.save(petBusinessType));

    }catch (Exception e){
      log.error("PetBusinessType Register Error: {}", e.getMessage());
      throw new RuntimeException("PetBusinessType Register Error");
    }
  }

  @Override
  @Transactional
  public PetBusinessTypeDTO updateType(PetBusinessTypeDTO petBusinessTypeDTO){
    try {
      if(!updateValidationType(petBusinessTypeDTO)){
        return null;
      }

      PetBusinessType petBusinessType =
          petBusinessTypeRepository.findById(petBusinessTypeDTO.getId()).orElse(null);
      if(petBusinessType == null){
        return null;
      }
      petBusinessType.setTypeName(petBusinessTypeDTO.getTypeName());
      petBusinessType.setDescription(petBusinessTypeDTO.getDescription());

      PetBusinessType updatePetBusinessType = petBusinessTypeRepository.save(petBusinessType);
      return petBusinessTypeMapper.toBasicDTO(updatePetBusinessType);

    }catch (Exception e){
      log.error("PetBusinessType Update Error: {}", e.getMessage());
      throw new RuntimeException("PetBusinessType Update Error");
    }
  }

  @Override
  @Transactional(readOnly = true)
  public PetBusinessTypeDTO getType(Long id) {
    PetBusinessType petBusinessType = petBusinessTypeRepository.findById(id).orElse(null);

    if(petBusinessType == null){
      return null;
    }

    return petBusinessTypeMapper.toBasicDTO(petBusinessType);
  }

  @Override
  @Transactional(readOnly = true)
  public List<PetBusinessTypeDTO> getTypeList() {
    List<PetBusinessType> petBusinessTypes = petBusinessTypeRepository.findAll();

    if (petBusinessTypes.isEmpty()) {
      return null;
    }

    return petBusinessTypes.stream()
      .map(petBusinessTypeMapper::toBasicDTO)
      .toList();

  }

  @Override
  @Transactional
  public boolean deleteType(Long id) {
    try {
      PetBusinessType petBusinessType = petBusinessTypeRepository.findById(id).orElse(null);

      if (petBusinessType == null) {
        return false;
      }

      if(petBusinessRepository.existsByPetBusinessType_Id(id)){
        return false;
      }

      petBusinessTypeRepository.deleteById(id);
      return true;

    }catch (Exception e){
      log.error("펫 사업 타입 삭제 중 예외 발생!: {}",e.getMessage());
      throw new RuntimeException("Type Delete Error");
    }
  }

  @Override
  public boolean insertValidationType(PetBusinessTypeDTO petBusinessTypeDTO){

    if(isBlank(petBusinessTypeDTO.getTypeName())
      || isBlank(petBusinessTypeDTO.getDescription())
      || isBlank(petBusinessTypeDTO.getSectorCode())
      || isBlank(petBusinessTypeDTO.getTypeCode())){
      return false;
    }

    if(petBusinessTypeRepository.existsByTypeCode(petBusinessTypeDTO.getTypeCode())){
      return false;
    }

    return true;
  }

  @Override
  public boolean updateValidationType(PetBusinessTypeDTO petBusinessTypeDTO){
    PetBusinessType petBusinessType = petBusinessTypeRepository.findById(petBusinessTypeDTO.getId()).orElse(null);

    if(petBusinessType == null){
      return false;
    }

    if( isBlank(petBusinessTypeDTO.getTypeName())
        || isBlank(petBusinessTypeDTO.getDescription())
        || isBlank(petBusinessTypeDTO.getSectorCode())
        || isBlank(petBusinessTypeDTO.getTypeCode())){
      return false;
    }

    if(petBusinessType.getTypeName().equals(petBusinessTypeDTO.getTypeName())
        && petBusinessType.getDescription().equals(petBusinessTypeDTO.getDescription())
        && petBusinessType.getSectorCode().equals(petBusinessTypeDTO.getSectorCode())
        && petBusinessType.getTypeCode().equals(petBusinessTypeDTO.getTypeCode())) {
      return false;
    }

    return true;
  }

  @Override
  public boolean isBlank(String str){
    return str == null || str.trim().isEmpty();
  }
}
