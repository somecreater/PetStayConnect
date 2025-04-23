package com.petservice.main.business.database.mapper;


import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.repository.PetBusinessTypeRepository;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetBusinessMapper {

  private final UserRepository userRepository;
  private final PetBusinessTypeRepository petBusinessTypeRepository;

  public PetBusiness toEntity(PetBusinessDTO petBusinessDTO){

    PetBusiness petBusiness=new PetBusiness();
    petBusiness.setId(petBusinessDTO.getId());
    petBusiness.setBusinessName(petBusinessDTO.getBusinessName());
    petBusiness.setStatus(petBusinessDTO.getStatus());
    petBusiness.setMinPrice(petBusinessDTO.getMinPrice());
    petBusiness.setMaxPrice(petBusinessDTO.getMaxPrice());
    petBusiness.setFacilities(petBusinessDTO.getFacilities());
    petBusiness.setDescription(petBusinessDTO.getDescription());
    petBusiness.setAvgRate(petBusinessDTO.getAvgRate());
    petBusiness.setRegistrationNumber(petBusinessDTO.getRegistrationNumber());
    petBusiness.setBankAccount(petBusinessDTO.getBankAccount());
    petBusiness.setVarification(petBusinessDTO.getVarification());

    if(petBusinessDTO.getUserId()!=null) {
      petBusiness.setUser(
          userRepository.findById(
              petBusinessDTO.getUserId()).orElse(null));
    }
    if(petBusinessDTO.getPetBusinessTypeId()!=null){
      petBusiness.setPetBusinessType(
          petBusinessTypeRepository.findById(
              petBusinessDTO.getPetBusinessTypeId()).orElse(null));
    }

    return petBusiness;
  }

  public PetBusinessDTO toDTO(PetBusiness petBusiness){

    PetBusinessDTO petBusinessDTO =new PetBusinessDTO();
    petBusinessDTO.setId(petBusiness.getId());
    petBusinessDTO.setBusinessName(petBusiness.getBusinessName());
    petBusinessDTO.setStatus(petBusiness.getStatus());
    petBusinessDTO.setMinPrice(petBusiness.getMinPrice());
    petBusinessDTO.setMaxPrice(petBusiness.getMaxPrice());
    petBusinessDTO.setFacilities(petBusiness.getFacilities());
    petBusinessDTO.setDescription(petBusiness.getDescription());
    petBusinessDTO.setAvgRate(petBusiness.getAvgRate());
    petBusinessDTO.setRegistrationNumber(petBusiness.getRegistrationNumber());
    petBusinessDTO.setBankAccount(petBusiness.getBankAccount());
    petBusinessDTO.setVarification(petBusiness.getVarification());

    if(petBusiness.getUser()!=null){
      petBusinessDTO.setUserId(petBusiness.getUser().getId());
    }
    if(petBusiness.getPetBusinessType()!=null){
      petBusinessDTO.setPetBusinessTypeName(petBusiness.getPetBusinessType().getTypeName());
      petBusinessDTO.setPetBusinessTypeId(petBusiness.getPetBusinessType().getId());
    }
    return petBusinessDTO;
  }
}
