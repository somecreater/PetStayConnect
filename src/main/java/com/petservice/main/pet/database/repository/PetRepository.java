package com.petservice.main.pet.database.repository;

import com.petservice.main.pet.database.entity.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    // 특정 회원의 펫 목록 조회
    List<Pet> findByUserId(Long userId);
}
