package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.PetBusinessTagDTO;

import java.util.List;

public interface PetBusinessTagServiceInterface {

  public List<PetBusinessTagDTO> readTagByBusinessId(Long business_id);
  public PetBusinessTagDTO readTagById(Long id);

  public PetBusinessTagDTO registerTag(PetBusinessTagDTO petBusinessTagDTO);
  public boolean deleteTag(Long id);
  public boolean deleteTagByBusinessId(Long business_id);
  public boolean ValidationTag(PetBusinessTagDTO petBusinessTagDTO);
  public boolean isBlank(String str);
}
