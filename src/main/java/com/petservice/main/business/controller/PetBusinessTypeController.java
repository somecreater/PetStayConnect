package com.petservice.main.business.controller;

import com.petservice.main.business.database.dto.PetBusinessTypeDTO;
import com.petservice.main.business.service.Interface.PetBusinessTypeServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/type")
@RequiredArgsConstructor
public class PetBusinessTypeController {

  private final PetBusinessTypeServiceInterface petBusinessTypeService;

  @PermitAll
  @GetMapping
  public ResponseEntity<?> getTypeList(){

    Map<String,Object> result = new HashMap<>();
    List<PetBusinessTypeDTO> petBusinessTypeDTOList = petBusinessTypeService.getTypeList();
    if(petBusinessTypeDTOList != null) {
      result.put("result", true);
      result.put("typeList", petBusinessTypeDTOList);
    }else{
      result.put("result", false);
      result.put("message", "타입 목록이 존재하지 않거나 가져오는 것에 실패 했습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @PermitAll
  @GetMapping("/{type_id}")
  public ResponseEntity<?> getTypeDetail(
    @PathVariable("type_id") Long type_id){

    Map<String,Object> result = new HashMap<>();
    PetBusinessTypeDTO type = petBusinessTypeService.getType(type_id);

    if(type != null){
      result.put("result", true);
      result.put("type", type);
    }else{
      result.put("result", false);
      result.put("message", "해당 타입이 존재하지 않습니다.");
    }

    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @PostMapping
  public ResponseEntity<?> registerType(
    @AuthenticationPrincipal CustomUserDetails principal,
    @RequestBody PetBusinessTypeDTO petBusinessTypeDTO){

    if(!principal.getAuthorities()
        .contains(new SimpleGrantedAuthority("MANAGER"))){
      throw new AccessDeniedException("해당 권한이 없습니다.");
    }

    Map<String, Object> result = new HashMap<>();
    PetBusinessTypeDTO type = petBusinessTypeService.registerType(petBusinessTypeDTO);

    if(type != null) {
      result.put("result", true);
      result.put("message","정상적으로 새로운 사업자 타입을 등록하였습니다.");
      result.put("type", type);
    }else{
      result.put("result", false);
      result.put("message", "새로운 타입 등록에 실패하였습니다. " +
          "이미 존재하는 타입이거나 값이 유효하지 않습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @PutMapping("/{type_id}")
  public ResponseEntity<?> updateType(
    @AuthenticationPrincipal CustomUserDetails principal,
    @PathVariable("type_id") Long typeId,
    @RequestBody PetBusinessTypeDTO petBusinessTypeDTO){

    if(!principal.getAuthorities()
        .contains(new SimpleGrantedAuthority("MANAGER"))){
      throw new AccessDeniedException("해당 권한이 없습니다.");
    }

    Map<String,Object> result = new HashMap<>();
    PetBusinessTypeDTO type=petBusinessTypeService.updateType(petBusinessTypeDTO);

    if(type != null) {
      result.put("result", true);
      result.put("message", "정상적으로 타입 수정을 완료하였습니다.");
      result.put("type", type);
    }else{
      result.put("result", false);
      result.put("message", "타입 수정에 실패하였습니다. " +
          "이미 존재하는 타입이거나 값이 유효하지 않습니다.");
    }

    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasRole('MANAGER')")
  @DeleteMapping("/{type_id}")
  public ResponseEntity<?> deleteType(
    @AuthenticationPrincipal CustomUserDetails principal,
    @PathVariable("type_id") Long type_id,
    @RequestBody PetBusinessTypeDTO petBusinessTypeDTO){

    if(!principal.getAuthorities()
        .contains(new SimpleGrantedAuthority("MANAGER"))){
      throw new AccessDeniedException("해당 권한이 없습니다.");
    }
    Map<String,Object> result = new HashMap<>();

    if(petBusinessTypeService.deleteType(type_id)){
      result.put("result", true);
      result.put("message", "정상적으로 해당 타입을 삭제하였습니다.");
      result.put("deleteType",petBusinessTypeDTO);
      return ResponseEntity.ok(result);
    }else{
      result.put("result", false);
      result.put("message", "해당 타입을 삭제하는 것에 실패하였습니다, " +
          "해당 타입의 사업자 존재 여부를 확인하거나 다시 재시도 해주세요!!");
      return ResponseEntity.ok(result);
    }
  }
}
