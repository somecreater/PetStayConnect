package com.petservice.main.business.database.mapper;

import com.petservice.main.business.database.dto.PetBusinessTagDTO;
import com.petservice.main.business.database.entity.PetBusinessTag;
import com.petservice.main.business.database.repository.PetBusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetBusinessTagMapper {

  public final PetBusinessMapper petBusinessMapper;
  public final PetBusinessRepository petBusinessRepository;

  public PetBusinessTag toEntity(PetBusinessTagDTO petBusinessTagDTO){
    PetBusinessTag petBusinessTag = new PetBusinessTag();
    petBusinessTag.setId(petBusinessTagDTO.getId());
    petBusinessTag.setTagName(petBusinessTagDTO.getTagName());
    petBusinessTag.setTagType(petBusinessTagDTO.getTagType());
    if(petBusinessTagDTO.getBusiness_id() != null){
      petBusinessTag.setPetBusiness(petBusinessRepository
          .findById(petBusinessTagDTO.getBusiness_id()).orElse(null));
    }else if(petBusinessTagDTO.getBusiness_name() != null){
      petBusinessTag.setPetBusiness(petBusinessRepository
          .findByBusinessName(petBusinessTagDTO.getBusiness_name()));
    }
    return petBusinessTag;
  }

  public PetBusinessTagDTO toDto(PetBusinessTag petBusinessTag){
    PetBusinessTagDTO petBusinessTagDTO = new PetBusinessTagDTO();
    petBusinessTagDTO.setId(petBusinessTag.getId());
    petBusinessTagDTO.setTagName(petBusinessTag.getTagName());
    petBusinessTagDTO.setTagType(petBusinessTag.getTagType());
    if(petBusinessTag.getPetBusiness() != null){
      petBusinessTagDTO.setBusiness_id(petBusinessTag.getPetBusiness().getId());
      petBusinessTagDTO.setBusiness_name(petBusinessTag.getPetBusiness().getBusinessName());
    }
    return petBusinessTagDTO;
  }
}
