package com.petservice.main.pet.database.mapper;

import com.petservice.main.pet.database.dto.PetDTO;
import com.petservice.main.pet.database.entity.Pet;
import org.springframework.stereotype.Component;

@Component("petPetMapper")
public class PetMapper {
    public PetDTO toDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setSpecies(pet.getSpecies());
        dto.setBreed(pet.getBreed());
        dto.setBirthDate(pet.getBirthDate());
        dto.setHealthInfo(pet.getHealthInfo());
        dto.setGender(pet.getGender());
        dto.setUserId(pet.getUserId());
        return dto;
    }

    public Pet toEntity(PetDTO dto) {
        Pet pet = new Pet();
        pet.setId(dto.getId());
        pet.setName(dto.getName());
        pet.setSpecies(dto.getSpecies());
        pet.setBreed(dto.getBreed());
        pet.setBirthDate(dto.getBirthDate());
        pet.setHealthInfo(dto.getHealthInfo());
        pet.setGender(dto.getGender());
        pet.setUserId(dto.getUserId());
        return pet;
    }
}
