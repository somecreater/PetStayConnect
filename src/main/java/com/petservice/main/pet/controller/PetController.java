package com.petservice.main.pet.controller;

import com.petservice.main.pet.database.dto.PetDTO;
import com.petservice.main.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    // 등록
    @PostMapping
    public PetDTO createPet(@RequestBody PetDTO petDTO) {
        return petService.createPet(petDTO);
    }
    // 수정
    @PutMapping("/{petId}")
    public PetDTO updatePet(@PathVariable Long petId, @RequestBody PetDTO petDTO) {
        return petService.updatePet(petId, petDTO);
    }
    // 삭제
    @DeleteMapping("/{petId}")
    public void deletePet(@PathVariable Long petId) {
        petService.deletePet(petId);
    }

    // 특정 회원의 펫 목록 조회
    @GetMapping("/user/{userId}")
    public List<PetDTO> getPetsByUserId(@PathVariable Long userId) {
        return petService.getPetsByUserId(userId);
    }

    // 펫 단건 조회
    @GetMapping("/{petId}")
    public PetDTO getPetById(@PathVariable Long petId) {
        return petService.getPetById(petId);
    }
}