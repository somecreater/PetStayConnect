package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.PetBusinessTypeDTO;

import java.util.List;

public interface PetBusinessTypeServiceInterface {

  public PetBusinessTypeDTO registerType(PetBusinessTypeDTO petBusinessTypeDTO);
  public PetBusinessTypeDTO updateType(PetBusinessTypeDTO petBusinessTypeDTO);
  public PetBusinessTypeDTO getType(Long id);
  public List<PetBusinessTypeDTO> getTypeList();
  public boolean deleteType(Long id);
  public boolean insertValidationType(PetBusinessTypeDTO petBusinessTypeDTO);
  public boolean updateValidationType(PetBusinessTypeDTO petBusinessTypeDTO);
  public boolean isBlank(String str);
}
