package com.petservice.main.pet.service;

import com.petservice.main.user.database.dto.PetDTO;
import com.petservice.main.user.database.entity.Pet;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.mapper.PetMapper;
import com.petservice.main.user.database.mapper.UserMapper;
import com.petservice.main.user.database.repository.PetRepository;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetServiceInterface {

    private final PetRepository petRepository;
    private final PetMapper petMapper;
    private final UserMapper userMapper;

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PetDTO> getPetsByUserLoginId(String userLoginId) {

        List<Pet> PetList= petRepository.findByUser_UserLoginId(userLoginId);
        if(PetList.isEmpty()){
            return null;
        }
        return PetList.stream().map(petMapper::toBasicDTO).toList();
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
    public PetDTO createPet(PetDTO petDTO, String userLoginId) {
        try {
            if (!insertValidationPet(petDTO)) {
                return null;
            }
            Pet pet = petMapper.toEntity(petDTO);

            // user 객체 세팅
            User user = userRepository.findByUserLoginId(userLoginId).orElse(null);

            if(user == null){
                return null;
            }

            pet.setUser(user);
            Pet savedPet = petRepository.save(pet);
            return petMapper.toDTO(savedPet);
        } catch (Exception e) {
            log.error("Pet 등록 중 예외 발생: {}", e.getMessage());
            throw new RuntimeException("Pet 등록 실패");
        }
    }

    @Override
    @Transactional
    public PetDTO updatePet(PetDTO petDTO) {
        try {
            if (!updateValidationPet(petDTO)) {
                return null;
            }
            Pet existingPet = petRepository.findById(petDTO.getId()).orElse(null);
            if (existingPet == null) {
                return null;
            }
            existingPet.setName(petDTO.getName());
            existingPet.setSpecies(petDTO.getSpecies());
            existingPet.setBreed(petDTO.getBreed());
            existingPet.setBirthDate(petDTO.getBirthDate());
            existingPet.setAge(petDTO.getAge());
            existingPet.setHealthInfo(petDTO.getHealthInfo());
            existingPet.setGender(petDTO.getGender());

            Pet updatedPet = petRepository.save(existingPet);
            return petMapper.toBasicDTO(updatedPet);
        } catch (Exception e) {
            log.error("Pet 수정 중 예외 발생: {}", e.getMessage());
            throw new RuntimeException("Pet 수정 실패");
        }
    }

    @Override
    @Transactional
    public boolean deletePet(Long petId) {
        try {
            if(!petRepository.existsById(petId)){
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
    @Override
    public boolean insertValidationPet(PetDTO petDTO) {
        if(isBlank(petDTO.getName())
            || isBlank(petDTO.getSpecies())
            || isBlank(petDTO.getBreed())
            || isBlank(petDTO.getGender().name())
        ){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean updateValidationPet(PetDTO petDTO) {
        Pet pet=petRepository.findById(petDTO.getId()).orElse(null);
        if(pet==null){
            return false;
        }

        if(isBlank(petDTO.getName())
            || isBlank(petDTO.getSpecies())
            || isBlank(petDTO.getBreed())
            || isBlank(petDTO.getGender().name())
        ){
            return false;
        }

        if(pet.getName().equals(petDTO.getName())
            && pet.getSpecies().equals(petDTO.getSpecies())
            && pet.getBreed().equals(petDTO.getBreed())
            && pet.getGender().equals(petDTO.getGender())){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserPet(String userLoginId, Long petId){

        PetDTO petDTO=getPetById(petId);
        if(petDTO == null){
            return false;
        }
        if(petDTO.getUserLoginId().compareTo(userLoginId) != 0){
            return false;
        }
        return true;
    }

    @Override
    public boolean isBlank(String str){
        return str == null || str.trim().isEmpty();
    }

}
