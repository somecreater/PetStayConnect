package com.petservice.main.common.controller;


import com.petservice.main.common.database.dto.CatBreedDTO;
import com.petservice.main.common.database.dto.DogBreedDTO;
import com.petservice.main.common.service.BreedServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/breed")
public class BreedController {

  public final BreedServiceInterface breedServiceInterface;

  @GetMapping("/cat")
  public ResponseEntity<?> getCatList(
      @AuthenticationPrincipal CustomUserDetails principal){
    Map<String,Object> result=new HashMap<>();
    List<CatBreedDTO> catBreedDTOS = breedServiceInterface.getCatBreedDtoList();
    if(catBreedDTOS.isEmpty()) {
      result.put("result", false);
      result.put("message", "묘종 리스트를 가져오는데 실패했습니다.");
    }else{
      result.put("result", true);
      result.put("message", "현재 DB내 저장된 묘종 리스트입니다!");
      result.put("catBreedList", catBreedDTOS);
    }

    return ResponseEntity.ok(result);
  }

  @GetMapping("/cat/{cat_id}")
  public ResponseEntity<?> getCat(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("cat_id") String cat_id){
    Map<String,Object> result=new HashMap<>();
    CatBreedDTO catBreedDTO = breedServiceInterface.getCatBreedDto(cat_id);
    if(catBreedDTO == null) {
      result.put("result", false);
      result.put("message", "묘종을 가져오는데 실패했습니다.");
    }else{
      result.put("result", true);
      result.put("message", "현재 DB내 저장된 묘종입니다!");
      result.put("catBreed", catBreedDTO);
    }

    return ResponseEntity.ok(result);
  }

  @GetMapping("/dog")
  public ResponseEntity<?> getDogList(
      @AuthenticationPrincipal CustomUserDetails principal) {
    Map<String,Object> result=new HashMap<>();
    List<DogBreedDTO> dogBreedDTOS = breedServiceInterface.getDogBreedDtoList();
    if(dogBreedDTOS.isEmpty()){
      result.put("result", false);
      result.put("message", "견종 리스트를 가져오는데 실패했습니다.");
    }else{
      log.info("dog data test: {}", dogBreedDTOS.get(10).getName());
      result.put("result", true);
      result.put("message", "현재 DB내 저장된 견종 리스트입니다!");
      result.put("dogBreedList", dogBreedDTOS);
    }

    return ResponseEntity.ok(result);
  }

  @GetMapping("/dog/{dog_id}")
  public ResponseEntity<?> getDog(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("dog_id") Integer dog_id){
    Map<String,Object> result=new HashMap<>();
    DogBreedDTO dogBreedDTO = breedServiceInterface.getDogBreedDto(dog_id);
    if(dogBreedDTO == null) {
      result.put("result", false);
      result.put("message", "견종을 가져오는데 실패했습니다.");
    }else{
      result.put("result", true);
      result.put("message", "현재 DB내 저장된 견종입니다!");
      result.put("dogBreed", dogBreedDTO);
    }

    return ResponseEntity.ok(result);
  }

}
