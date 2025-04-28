package com.petservice.main.business.service.Interface;

import com.petservice.main.business.database.dto.PetBusinessDTO;

public interface PetBusinessServiceInterface {
  public boolean BusinessValidation(PetBusinessDTO petBusinessDTO);
  public boolean existBusiness(PetBusinessDTO petBusinessDTO);
}
