package com.petservice.main.business.controller;


import com.petservice.main.business.database.dto.PetBusinessTagDTO;
import com.petservice.main.business.service.Interface.PetBusinessTagServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.dto.UserDTO;
import com.petservice.main.user.service.Interface.CustomUserServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tag")
@RequiredArgsConstructor
public class PetBusinessTagController {

  private final PetBusinessTagServiceInterface petBusinessTagServiceInterface;
  private final CustomUserServiceInterface customUserServiceInterface;

  @GetMapping("/business/{business_id}")
  public ResponseEntity<?> getTagList(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("business_id") Long business_id){
    Map<String,Object> result = new HashMap<>();
    List<PetBusinessTagDTO> petBusinessTagDTOS =
        petBusinessTagServiceInterface.readTagByBusinessId(business_id);
    if(petBusinessTagDTOS == null){
      result.put("result", false);
      result.put("message","태그가 존재하지 않습니다,");
    }else {
      result.put("result", true);
      result.put("message",business_id+" 아이디의 태그를 가져옵니다.");
      result.put("tags",petBusinessTagDTOS);
    }

    return ResponseEntity.ok(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<?> getTag(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("id") Long id){
    Map<String,Object> result = new HashMap<>();
    PetBusinessTagDTO petBusinessTagDTO = petBusinessTagServiceInterface.readTagById(id);
    if(petBusinessTagDTO == null){
      result.put("result", false);
      result.put("message", "태그가 존재하지 않습니다.");
    }else{
      result.put("result", true);
      result.put("message", "태그를 가져옵니다.");
      result.put("tag",petBusinessTagDTO);
    }

    return ResponseEntity.ok(result);
  }

  @PostMapping
  public ResponseEntity<?> registerTag(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestBody PetBusinessTagDTO petBusinessTagDTO){
    Map<String,Object> result = new HashMap<>();
    UserDTO userDTO = customUserServiceInterface.getUserByLoginId(principal.getUsername());
    String business_name= userDTO.getPetBusinessDTO().getBusinessName();
    if(business_name.compareTo(petBusinessTagDTO.getBusiness_name())!=0){
      result.put("result", false);
      result.put("message", "권한이 없습니다.");
    }else{
      PetBusinessTagDTO insertTag = petBusinessTagServiceInterface.registerTag(petBusinessTagDTO);
      if(insertTag == null){
        result.put("result", false);
        result.put("message", "태그 입력에 실패했습니다.");
      }else{
        result.put("result", true);
        result.put("message", "태그가 정상적으로 입력되었습니다.");
        result.put("insert",insertTag);
      }
    }

    return ResponseEntity.ok(result);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> deleteTag(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("id") Long id){
    Map<String,Object> result = new HashMap<>();
    if(!petBusinessTagServiceInterface.deleteTag(id)){
      result.put("result", false);
      result.put("message", "태그 삭제에 실패했습니다.");
    }else{
      result.put("result", true);
      result.put("message", "태그 삭제를 성공했습니다.");
    }

    return ResponseEntity.ok(result);
  }
}
