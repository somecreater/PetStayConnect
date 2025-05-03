package com.petservice.main.business.controller;

import com.petservice.main.business.database.dto.NaverSearchRequest;
import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.dto.SearchRequest;
import com.petservice.main.business.service.Interface.NaverSearchServiceInterface;
import com.petservice.main.business.service.Interface.PetBusinessServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/provider")
@RequiredArgsConstructor
public class PetBusinessController {

  private final NaverSearchServiceInterface naverSearchService;
  private final PetBusinessServiceInterface petBusinessService;

  @GetMapping("/list")
  public ResponseEntity<?> getPetBusinessList(
    @AuthenticationPrincipal CustomUserDetails principal,
    @RequestBody SearchRequest request,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "5") int size ){

    Map<String,Object> result = new HashMap<>();

    if(request == null){
      log.info("적절한 검색 쿼리가 존재하지 않습니다.");
      result.put("result",false);
      result.put("message","적절한 검색 쿼리가 존재하지 않습니다.");
      return ResponseEntity.badRequest().body(result);
    }

    Page<PetBusinessDTO> petBusinessDTOS=petBusinessService.getBusinessList(
        request.getBusinessName(),
        request.getSectorCode(),
        request.getTypeCode(),
        principal.getUsername(),
        request.is_around(),
        page,size);

    if(petBusinessDTOS.getTotalElements() == 0){
      log.info("결과가 존재하지 않습니다.");
      result.put("result",false);
      result.put("message","결과가 존재하지 않습니다.");
    }else{
      log.info("총 {} 건의 결과를 가지고 왔습니다. ",petBusinessDTOS.getTotalElements());
      result.put("result",true);
      String SearchRequest="사업체 명: "+request.getBusinessName()+", 섹터 코드: "+ request.getSectorCode()+
          ", 타입 코드: "+request.getTypeCode()+", 주변 여부: "+ request.is_around();
      result.put("message","검색 쿼리: " + SearchRequest);
      result.put("search", petBusinessDTOS);
    }

    return ResponseEntity.ok(result);
  }


  @PreAuthorize("isAuthenticated()")
  @GetMapping("/list/naver")
  public ResponseEntity<?> getPetBusinessList(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestBody NaverSearchRequest request,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size ){

    Map<String,Object> result = new HashMap<>();
    Pageable pageable= PageRequest.of(page, size);

    if(request == null){
      log.info("적절한 검색 쿼리가 존재하지 않습니다.");
      result.put("result",false);
      result.put("message","적절한 검색 쿼리가 존재하지 않습니다.");
      return ResponseEntity.badRequest().body(result);
    }

    Page<PetBusinessDTO> petBusinessDTOS=
        naverSearchService.searchNearPyBusinessDTO(principal.getUsername(), request, pageable);

    if(petBusinessDTOS.getTotalElements() == 0){
      log.info("결과가 존재하지 않습니다.");
      result.put("result",false);
      result.put("message","결과가 존재하지 않습니다.");
    }else{
      log.info("총 {} 건의 결과를 가지고 왔습니다. ",petBusinessDTOS.getTotalElements());
      result.put("result",true);
      String SearchRequest="사업체 명: "+request.getBusinessName()+", 섹터 코드: "+ request.getSectorCode()+
          ", 타입 코드: "+request.getTypeCode()+", 주변 여부: "+ request.isNear();
      result.put("message","검색 쿼리: " + SearchRequest);
      result.put("search", petBusinessDTOS);
    }

    return ResponseEntity.ok(result);
  }
}
