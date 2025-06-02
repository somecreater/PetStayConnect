package com.petservice.main.common.service;

import com.petservice.main.common.database.dto.CatBreedDTO;
import com.petservice.main.common.database.dto.DogBreedDTO;
import com.petservice.main.common.database.entity.CatBreed;
import com.petservice.main.common.database.entity.DogBreed;
import com.petservice.main.common.database.mapper.CatBreedMapper;
import com.petservice.main.common.database.mapper.DogBreedMapper;
import com.petservice.main.common.database.repository.CatBreedRepository;
import com.petservice.main.common.database.repository.DogBreedRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BreedService implements BreedServiceInterface{

  private final CatBreedRepository catBreedRepository;
  private final DogBreedRepository dogBreedRepository;

  private final CatBreedMapper catBreedMapper;
  private final DogBreedMapper dogBreedMapper;

  @Override
  @Transactional(readOnly = true)
  public List<CatBreedDTO> getCatBreedDtoList() {
    List<CatBreed> catBreeds = catBreedRepository.findAll();
    return catBreeds.stream().map(catBreedMapper::toDTO).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public CatBreedDTO getCatBreedDto(String cat_id) {
    CatBreed catBreed = catBreedRepository.findById(cat_id).orElse(null);
    if(catBreed == null){
      return null;
    }
    return catBreedMapper.toDTO(catBreed);
  }

  @Override
  @Transactional(readOnly = true)
  public List<DogBreedDTO> getDogBreedDtoList() {
    List<DogBreed> dogBreeds = dogBreedRepository.findAll();
    return dogBreeds.stream().map(dogBreedMapper::toDTO).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public DogBreedDTO getDogBreedDto(Integer dog_id) {
    DogBreed dogBreed = dogBreedRepository.findById(dog_id).orElse(null);
    if(dogBreed == null){
      return null;
    }
    return dogBreedMapper.toDTO(dogBreed);
  }
}
