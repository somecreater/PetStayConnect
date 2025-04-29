package com.petservice.main.pet.controller;

import com.petservice.main.user.database.dto.PetDTO;
import com.petservice.main.pet.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    // 등록
    @PostMapping
    public ResponseEntity<?> createPet(@RequestBody PetDTO petDTO) {
        Map<String, Object> result = new HashMap<>();
        PetDTO createdPet = petService.createPet(petDTO);
        if (createdPet != null) {
            result.put("result", true);
            result.put("message", "펫 등록 성공");
            result.put("pet", createdPet);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } else {
            result.put("result", false);
            result.put("message", "펫 등록 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    // 수정
    @PutMapping("/{petId}")
    public ResponseEntity<?> updatePet(@PathVariable Long petId, @RequestBody PetDTO petDTO) {
        Map<String, Object> result = new HashMap<>();
        PetDTO updatedPet = petService.updatePet(petId, petDTO);
        if (updatedPet != null) {
            result.put("result", true);
            result.put("message", "펫 수정 성공");
            result.put("pet", updatedPet);
            return ResponseEntity.ok(result);
        } else {
            result.put("result", false);
            result.put("message", "펫 수정 실패");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }

    // 삭제
    @DeleteMapping("/{petId}")
    public ResponseEntity<?> deletePet(@PathVariable Long petId) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean deleted = petService.deletePet(petId);
            if (deleted) {
                return ResponseEntity.noContent().build(); // 204 (body 없음)
            } else {
                result.put("result", false);
                result.put("message", "펫 삭제 실패: 존재하지 않는 펫");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
            }
        } catch (Exception e) {
            result.put("result", false);
            result.put("message", "펫 삭제 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 특정 회원의 펫 목록 조회
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPetsByUserId(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<PetDTO> pets = petService.getPetsByUserId(userId);
        result.put("result", true);
        result.put("pets", pets);
        return ResponseEntity.ok(result);
    }

    // 펫 단건 조회
    @GetMapping("/{petId}")
    public ResponseEntity<?> getPetById(@PathVariable Long petId) {
        Map<String, Object> result = new HashMap<>();
        PetDTO pet = petService.getPetById(petId);
        if (pet != null) {
            result.put("result", true);
            result.put("pet", pet);
            return ResponseEntity.ok(result);
        } else {
            result.put("result", false);
            result.put("message", "해당 펫이 존재하지 않습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }
}