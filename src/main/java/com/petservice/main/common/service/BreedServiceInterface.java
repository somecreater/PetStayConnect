package com.petservice.main.common.service;

import com.petservice.main.common.database.dto.CatBreedDTO;
import com.petservice.main.common.database.dto.DogBreedDTO;

import java.util.List;

public interface BreedServiceInterface {

  public List<CatBreedDTO> getCatBreedDtoList();
  public CatBreedDTO getCatBreedDto(String cat_id);
  public List<DogBreedDTO> getDogBreedDtoList();
  public DogBreedDTO getDogBreedDto(Integer dog_id);
}
