package com.petservice.main.common.database.mapper;

import com.petservice.main.common.database.dto.DogBreedDTO;
import com.petservice.main.common.database.entity.DogBreed;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DogBreedMapper {

  public DogBreed toEntity(DogBreedDTO dogBreedDTO){
    DogBreed dogBreed= new DogBreed();
    dogBreed.setId(dogBreedDTO.getId());
    dogBreed.setName(dogBreedDTO.getName());
    dogBreed.setTemperament(dogBreedDTO.getTemperament());
    dogBreed.setLifeSpan(dogBreedDTO.getLifeSpan());
    return dogBreed;
  }

  public DogBreedDTO toDTO(DogBreed dogBreed){
    DogBreedDTO dogBreedDTO= new DogBreedDTO();
    dogBreedDTO.setId(dogBreed.getId());
    dogBreedDTO.setName(dogBreedDTO.getName());
    dogBreedDTO.setTemperament(dogBreed.getTemperament());
    dogBreedDTO.setLifeSpan(dogBreed.getLifeSpan());
    return dogBreedDTO;
  }
}
