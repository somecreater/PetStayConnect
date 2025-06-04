package com.petservice.main.business.service;

import com.petservice.main.business.database.dto.PetBusinessTagDTO;
import com.petservice.main.business.database.entity.PetBusinessTag;
import com.petservice.main.business.database.mapper.PetBusinessTagMapper;
import com.petservice.main.business.database.repository.PetBusinessTagRepository;
import com.petservice.main.business.service.Interface.PetBusinessTagServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetBusinessTagService implements PetBusinessTagServiceInterface {

  private final PetBusinessTagRepository petBusinessTagRepository;

  private final PetBusinessTagMapper petBusinessTagMapper;

  @Override
  @Transactional(readOnly = true)
  public List<PetBusinessTagDTO> readTagByBusinessId(Long business_id) {
    List<PetBusinessTag> petBusinessTags = petBusinessTagRepository
        .findByPetBusiness_Id(business_id);
    return petBusinessTags.stream().map(petBusinessTagMapper::toDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public PetBusinessTagDTO readTagById(Long id) {
    PetBusinessTag petBusinessTag = petBusinessTagRepository.findById(id).orElse(null);
    if(petBusinessTag == null){
      return null;
    }
    return petBusinessTagMapper.toDto(petBusinessTag);
  }

  @Override
  @Transactional
  public PetBusinessTagDTO registerTag(PetBusinessTagDTO petBusinessTagDTO) {
    if(!ValidationTag(petBusinessTagDTO)){
      return null;
    }
    if(petBusinessTagRepository
        .existsByTagNameAndTagTypeAndPetBusiness_IdAndPetBusiness_BusinessName(
            petBusinessTagDTO.getTagName(),
            petBusinessTagDTO.getTagType(),
            petBusinessTagDTO.getBusiness_id(),
            petBusinessTagDTO.getBusiness_name()
        )){
      return null;
    }
    PetBusinessTag insert=petBusinessTagRepository.save(
        petBusinessTagMapper.toEntity(petBusinessTagDTO));
    return petBusinessTagMapper.toDto(insert);
  }

  @Override
  public boolean deleteTag(Long id) {
    try {
      petBusinessTagRepository.deleteById(id);
      return true;
    }catch (Exception e){
      log.info(e.getMessage());
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean ValidationTag(PetBusinessTagDTO petBusinessTagDTO) {

    if(petBusinessTagDTO == null){
      return false;
    }

    if(isBlank(petBusinessTagDTO.getTagName())
    || isBlank(petBusinessTagDTO.getBusiness_name())
    || petBusinessTagDTO.getTagType() == null
    || petBusinessTagDTO.getBusiness_id() ==null){
      return false;
    }

    return true;
  }

  @Override
  public boolean isBlank(String str){
    return str == null || str.trim().isEmpty();
  }
}
