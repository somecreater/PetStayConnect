package com.petservice.main.pet.service;

import com.petservice.main.user.database.dto.PetDTO;
import com.petservice.main.user.database.entity.Pet;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.mapper.PetMapper;
import com.petservice.main.user.database.repository.PetRepository;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PetMapper petMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PetDTO> getPetsByUserId(Long userId) {
        return petRepository.findAll().stream()
                .filter(pet -> pet.getUser() != null && pet.getUser().getId().equals(userId))
                .map(petMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PetDTO getPetById(Long petId) {
        Pet pet = petRepository.findById(petId).orElse(null);
        if (pet == null) return null;
        return petMapper.toDTO(pet);
    }

    @Override
    @Transactional
    public PetDTO createPet(PetDTO petDTO) {
        try {
            if (!insertValidationPet(petDTO)) {
                return null;
            }
            Pet pet = petMapper.toEntity(petDTO);
            // user 객체 세팅
            if (petDTO.getUserId() != null) {
                User user = userRepository.findById(petDTO.getUserId()).orElse(null);
                pet.setUser(user);
            }
            Pet savedPet = petRepository.save(pet);
            return petMapper.toDTO(savedPet);
        } catch (Exception e) {
            log.error("Pet 등록 중 예외 발생: {}", e.getMessage());
            throw new RuntimeException("Pet 등록 실패");
        }
    }

    @Override
    @Transactional
    public PetDTO updatePet(Long petId, PetDTO petDTO) {
        try {
            if (!updateValidationPet(petDTO)) {
                return null;
            }
            Pet existingPet = petRepository.findById(petId).orElse(null);
            if (existingPet == null) {
                return null;
            }
            existingPet.setName(petDTO.getName());
            existingPet.setSpecies(petDTO.getSpecies());
            existingPet.setBreed(petDTO.getBreed());
            existingPet.setBirthDate(petDTO.getBirthDate());
            existingPet.setHealthInfo(petDTO.getHealthInfo());
            existingPet.setGender(petDTO.getGender());
            if (petDTO.getUserId() != null) {
                User user = userRepository.findById(petDTO.getUserId()).orElse(null);
                existingPet.setUser(user);
            }
            Pet updatedPet = petRepository.save(existingPet);
            return petMapper.toDTO(updatedPet);
        } catch (Exception e) {
            log.error("Pet 수정 중 예외 발생: {}", e.getMessage());
            throw new RuntimeException("Pet 수정 실패");
        }
    }

    @Override
    @Transactional
    public boolean deletePet(Long petId) {
        try {
            Pet pet = petRepository.findById(petId).orElse(null);
            if (pet == null) {
                return false;
            }
            petRepository.deleteById(petId);
            return true;
        } catch (Exception e) {
            log.error("Pet 삭제 중 예외 발생: {}", e.getMessage());
            throw new RuntimeException("Pet 삭제 실패");
        }
    }

    // --- 유효성 검사 메서드 예시 ---
    public boolean insertValidationPet(PetDTO petDTO) {
        return petDTO != null
                && petDTO.getName() != null && !petDTO.getName().trim().isEmpty()
                && petDTO.getUserId() != null;
    }

    public boolean updateValidationPet(PetDTO petDTO) {
        return petDTO != null
                && petDTO.getId() != null
                && petDTO.getName() != null && !petDTO.getName().trim().isEmpty();
    }
}
