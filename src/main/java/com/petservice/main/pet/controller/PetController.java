package com.petservice.main.pet.controller;

import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.dto.PetDTO;
import com.petservice.main.pet.service.PetServiceInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetServiceInterface petService;

    // 등록
    @PostMapping
    public ResponseEntity<?> createPet(
        @AuthenticationPrincipal CustomUserDetails principal,
        @RequestBody PetDTO petDTO) {
        Map<String, Object> result = new HashMap<>();
        if(principal == null){
            result.put("result", false);
            result.put("message", "펫 등록 실패(비로그인 상태)");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        PetDTO createdPet = petService.createPet(petDTO, principal.getUsername());
        if (createdPet != null) {
            result.put("result", true);
            result.put("message", "펫 등록 성공");
            result.put("pet", createdPet);
            return ResponseEntity.ok(result);
        } else {
            result.put("result", false);
            result.put("message", "펫 등록 실패");
            return ResponseEntity.ok(result);
        }
    }

    // 수정(본인 애완동물만 수정)
    @PutMapping("/{petId}")
    public ResponseEntity<?> updatePet(
        @AuthenticationPrincipal CustomUserDetails principal,
        @PathVariable("petId") Long petId,
        @RequestBody PetDTO petDTO) {

        Map<String, Object> result = new HashMap<>();
        if(principal == null){
            result.put("result", false);
            result.put("message", "펫 수정 실패(권한 없음)");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        String userLoginId = principal.getUsername();
        if(!Objects.equals(petDTO.getId(), petId) || !petService.isUserPet(userLoginId,petId)){
            result.put("result", false);
            result.put("message", "펫 수정 실패(권한 없음)");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        PetDTO updatedPet = petService.updatePet(petDTO);
        if (updatedPet != null) {
            result.put("result", true);
            result.put("message", "펫 수정 성공");
            result.put("pet", updatedPet);
            return ResponseEntity.ok(result);
        } else {
            result.put("result", false);
            result.put("message", "펫 수정 실패");
            return ResponseEntity.ok(result);
        }
    }

    // 삭제(본인 펫만 삭제)
    @DeleteMapping("/{petId}")
    public ResponseEntity<?> deletePet(
        @AuthenticationPrincipal CustomUserDetails principal,
        @PathVariable("petId") Long petId) {
        Map<String, Object> result = new HashMap<>();
        try {
            if( principal == null || !petService.isUserPet(principal.getUsername(),petId) ){
                result.put("result",false);
                result.put("message","비로그인 상태이거나 권한이 없습니다.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
            }

            boolean deleted = petService.deletePet(petId);
            if (deleted) {
                result.put("result",true);
                result.put("message","삭제 성공!!!!!");
                return ResponseEntity.ok(result); // 204 (body 없음)
            } else {
                result.put("result", false);
                result.put("message", "펫 삭제 실패: 존재하지 않는 펫");
                return ResponseEntity.ok(result);
            }
        } catch (Exception e) {
            result.put("result", false);
            result.put("message", "펫 삭제 실패: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(result);
        }
    }

    // 특정 회원의 펫 목록 조회
    @GetMapping("/user")
    public ResponseEntity<?> getPetsByUserId(
        @AuthenticationPrincipal CustomUserDetails principal) {
        Map<String, Object> result = new HashMap<>();
        List<PetDTO> pets = petService.getPetsByUserLoginId(principal.getUsername());
        if(pets !=null) {
            result.put("result", true);
            result.put("pets", pets);
        }else{
            result.put("result", false);
        }
        return ResponseEntity.ok(result);
    }

    // 펫 단건 조회
    @GetMapping("/{petId}")
    public ResponseEntity<?> getPetById(
        @AuthenticationPrincipal CustomUserDetails principal,
        @PathVariable("petId") Long petId) {

        Map<String, Object> result = new HashMap<>();
        if(!petService.isUserPet(principal.getUsername(),petId)){
            result.put("result", false);
            result.put("message", "해당 펫을 볼 권한이 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        PetDTO pet = petService.getPetById(petId);
        if (pet != null) {
            result.put("result", true);
            result.put("pet", pet);
            return ResponseEntity.ok(result);
        } else {
            result.put("result", false);
            result.put("message", "해당 펫이 존재하지 않습니다.");
            return ResponseEntity.ok(result);
        }
    }
}
