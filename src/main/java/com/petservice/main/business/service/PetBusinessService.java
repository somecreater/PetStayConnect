package com.petservice.main.business.service;

import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.service.Interface.PetBusinessServiceInterface;
import org.springframework.stereotype.Service;

@Service
public class PetBusinessService implements PetBusinessServiceInterface {

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
