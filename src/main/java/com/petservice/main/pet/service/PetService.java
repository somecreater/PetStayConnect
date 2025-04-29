package com.petservice.main.pet.service;

import com.petservice.main.user.database.dto.PetDTO;
import java.util.List;

public interface PetService {

    // 펫 등록
    PetDTO createPet(PetDTO petDTO);

    // 펫 수정
    PetDTO updatePet(Long petId, PetDTO petDTO);

    // 펫 삭제
    void deletePet(Long petId);

    // 특정 회원의 펫 목록 조회
    List<PetDTO> getPetsByUserId(Long userId);

    // 펫 단건 조회
    PetDTO getPetById(Long petId);
}