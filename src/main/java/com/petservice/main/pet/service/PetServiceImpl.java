package com.petservice.main.pet.service;

import com.petservice.main.pet.database.dto.PetDTO;
import com.petservice.main.pet.database.entity.Pet;
import com.petservice.main.pet.database.mapper.PetMapper;
import com.petservice.main.pet.database.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Override
    public List<PetDTO> getPetsByUserId(Long userId) {
        // 모든 펫 중 userId가 일치하는 것만 반환
        return petRepository.findAll().stream()
                .filter(pet -> pet.getUserId() != null && pet.getUserId().equals(userId))
                .map(petMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PetDTO getPetById(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("해당 펫이 존재하지 않습니다. id=" + petId));
        return petMapper.toDTO(pet);
    }
    @Override
    public PetDTO createPet(PetDTO petDTO) {
        Pet pet = petMapper.toEntity(petDTO);
        Pet savedPet = petRepository.save(pet);
        return petMapper.toDTO(savedPet);
    }
    @Override
    public PetDTO updatePet(Long petId, PetDTO petDTO) {
        Pet existingPet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("해당 펫이 존재하지 않습니다. id=" + petId));

        // 필요한 필드만 업데이트
        existingPet.setName(petDTO.getName());
        existingPet.setSpecies(petDTO.getSpecies());
        existingPet.setBreed(petDTO.getBreed());
        existingPet.setBirthDate(petDTO.getBirthDate());
        existingPet.setHealthInfo(petDTO.getHealthInfo());
        existingPet.setGender(petDTO.getGender());
        existingPet.setUserId(petDTO.getUserId());

        Pet updatedPet = petRepository.save(existingPet);
        return petMapper.toDTO(updatedPet);
    }
    @Override
    public void deletePet(Long petId) {
        if (!petRepository.existsById(petId)) {
            throw new IllegalArgumentException("해당 펫이 존재하지 않습니다. id=" + petId);
        }
        petRepository.deleteById(petId);
    }

}