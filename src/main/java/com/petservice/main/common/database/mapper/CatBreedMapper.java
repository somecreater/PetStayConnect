package com.petservice.main.common.database.mapper;

import com.petservice.main.common.database.dto.CatBreedDTO;
import com.petservice.main.common.database.entity.CatBreed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CatBreedMapper {

  public CatBreed toEntity(CatBreedDTO catBreedDTO){
    CatBreed catBreed= new CatBreed();
    catBreed.setId(catBreedDTO.getId());
    catBreed.setName(catBreedDTO.getName());
    catBreed.setOrigin(catBreedDTO.getOrigin());
    catBreed.setTemperament(catBreedDTO.getTemperament());
    catBreed.setLifeSpan(catBreedDTO.getLifeSpan());
    return catBreed;
  }

  public CatBreedDTO toDTO(CatBreed catBreed){
    CatBreedDTO catBreedDTO= new CatBreedDTO();
    catBreedDTO.setId(catBreed.getId());
    catBreedDTO.setName(catBreed.getName());
    catBreedDTO.setOrigin(catBreed.getOrigin());
    catBreedDTO.setTemperament(catBreed.getTemperament());
    catBreedDTO.setLifeSpan(catBreed.getLifeSpan());
    return catBreedDTO;
  }
}
