package com.petservice.main.pet.service;

import com.petservice.main.user.database.dto.PetDTO;
import java.util.List;

public interface PetServiceInterface {

    // 펫 등록
    PetDTO createPet(PetDTO petDTO, String userLoginId);

    // 펫 수정
    PetDTO updatePet(PetDTO petDTO);

    // 펫 삭제
    boolean deletePet(Long petId);

    // 특정 회원의 펫 목록 조회
    public List<PetDTO> getPetsByUserLoginId(String userLoginId);

    // 펫 단건 조회
    PetDTO getPetById(Long petId);

    // 데이터 검증
    public boolean insertValidationPet(PetDTO petDTO);

    public boolean updateValidationPet(PetDTO petDTO);

    public boolean isUserPet(String userLoginId, Long petId);

    public boolean isBlank(String s);
}
