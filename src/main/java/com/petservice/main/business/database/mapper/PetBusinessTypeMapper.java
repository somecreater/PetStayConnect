package com.petservice.main.business.database.mapper;

import com.petservice.main.business.database.dto.PetBusinessTypeDTO;
import com.petservice.main.business.database.entity.PetBusinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetBusinessTypeMapper {

  public final PetBusinessMapper petBusinessMapper;

  public PetBusinessType toEntity(PetBusinessTypeDTO petBusinessTypeDTO){

    PetBusinessType petBusinessType=new PetBusinessType();
    petBusinessType.setId(petBusinessTypeDTO.getId());
    petBusinessType.setTypeName(petBusinessTypeDTO.getTypeName());
    petBusinessType.setDescription(petBusinessTypeDTO.getDescription());
    if(petBusinessTypeDTO.getPetBusinessDTOList()!=null) {
      petBusinessType.setPetBusinessList(
          petBusinessTypeDTO.getPetBusinessDTOList()
              .stream().map(petBusinessMapper::toEntity).toList());
    }

    return petBusinessType;
  }

  public PetBusinessTypeDTO toDTO(PetBusinessType petBusinessType){

    PetBusinessTypeDTO petBusinessTypeDTO =new PetBusinessTypeDTO();
    petBusinessTypeDTO.setId(petBusinessType.getId());
    petBusinessTypeDTO.setTypeName(petBusinessType.getTypeName());
    petBusinessTypeDTO.setDescription(petBusinessType.getDescription());
    if(petBusinessType.getPetBusinessList()!=null) {
      petBusinessTypeDTO.setPetBusinessDTOList(
          petBusinessType.getPetBusinessList()
              .stream().map(petBusinessMapper::toDTO).toList());
    }
    return petBusinessTypeDTO;
  }

  public PetBusinessTypeDTO toBasicDTO(PetBusinessType petBusinessType){

    PetBusinessTypeDTO petBusinessTypeDTO =new PetBusinessTypeDTO();
    petBusinessTypeDTO.setId(petBusinessType.getId());
    petBusinessTypeDTO.setTypeName(petBusinessType.getTypeName());
    petBusinessTypeDTO.setDescription(petBusinessType.getDescription());
    return petBusinessTypeDTO;
  }
}
