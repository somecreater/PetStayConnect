package com.petservice.main.user.database.mapper;

import com.petservice.main.user.database.dto.PetDTO;
import com.petservice.main.user.database.entity.Pet;
import com.petservice.main.user.database.repository.PetRepository;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PetMapper {

  private final PetRepository petRepository;
  private final UserRepository userRepository;

  public Pet toEntity(PetDTO petDTO){

    Pet pet=new Pet();
    pet.setId(petDTO.getId());
    pet.setName(petDTO.getName());
    pet.setSpecies(petDTO.getSpecies());
    pet.setBreed(petDTO.getBreed());
    pet.setBirthDate(petDTO.getBirthDate());
    pet.setHealthInfo(petDTO.getHealthInfo());
    pet.setGender(petDTO.getGender());
    pet.setCreatedAt(petDTO.getCreateAt());
    pet.setUpdatedAt(petDTO.getUpdateAt());
    if(petDTO.getUserId() != null) {
      pet.setUser(userRepository.findById(petDTO.getUserId()).orElse(null));
    }
    return pet;
  }

  public PetDTO toDTO(Pet pet){

    PetDTO petDTO =new PetDTO();
    petDTO.setId(pet.getId());
    petDTO.setName(pet.getName());
    petDTO.setSpecies(pet.getSpecies());
    petDTO.setBreed(pet.getBreed());
    petDTO.setBirthDate(pet.getBirthDate());
    petDTO.setHealthInfo(pet.getHealthInfo());
    petDTO.setGender(pet.getGender());
    petDTO.setCreateAt(pet.getCreatedAt());
    petDTO.setUpdateAt(pet.getUpdatedAt());
    if(pet.getUser() != null){
      petDTO.setUserId(pet.getUser().getId());
    }
    return petDTO;
  }
  public PetDTO toBasicDTO(Pet pet){

    PetDTO petDTO =new PetDTO();
    petDTO.setId(pet.getId());
    petDTO.setName(pet.getName());
    petDTO.setSpecies(pet.getSpecies());
    petDTO.setBreed(pet.getBreed());
    petDTO.setBirthDate(pet.getBirthDate());
    petDTO.setHealthInfo(pet.getHealthInfo());
    petDTO.setGender(pet.getGender());
    petDTO.setCreateAt(pet.getCreatedAt());
    petDTO.setUpdateAt(pet.getUpdatedAt());

    return petDTO;
  }
}
