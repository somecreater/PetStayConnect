package com.petservice.main.api.controller;

import com.petservice.main.api.database.dto.Kakao.GeolocationResponse;
import com.petservice.main.api.database.dto.Kakao.KakaoLocalRequest;
import com.petservice.main.api.database.dto.Kakao.KakaoPlaceDTO;
import com.petservice.main.api.database.dto.Kakao.LocalSearchType;
import com.petservice.main.api.service.Interface.KakaoLocalServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/recommend")
public class RecommendController {

  private final KakaoLocalServiceInterface kakaoLocalServiceInterface;

  @GetMapping("/getLocation")
  public ResponseEntity<?> getLocation(){
    Map<String,Object> result = new HashMap<>();
    GeolocationResponse geolocationResponse= kakaoLocalServiceInterface.geolocate();

    if(geolocationResponse != null){
      result.put("result", true);
      result.put("message", "위치정보를 전송합니다.");
      result.put("location", geolocationResponse);
    }else{
      result.put("result",false);
      result.put("message", "위치정보를 얻어오는것을 실패하였습니다.");
    }
    return ResponseEntity.ok(result);
  }

  @GetMapping
  public ResponseEntity<?> getRecommendPlace(
      @RequestParam double latitude,
      @RequestParam double longitude,
      @RequestParam(defaultValue = "1000") int distance,
      @RequestParam(defaultValue = "1") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(defaultValue = "HOTEL") String searchType){
    Map<String,Object> result = new HashMap<>();

    KakaoLocalRequest kakaoLocalRequest=new KakaoLocalRequest();
    kakaoLocalRequest.setLatitude(latitude);
    kakaoLocalRequest.setLongitude(longitude);
    kakaoLocalRequest.setDistance(distance);
    kakaoLocalRequest.setPage(page);
    kakaoLocalRequest.setSize(size);
    if(searchType.compareTo("HOTEL") == 0){
      kakaoLocalRequest.setSearchType(LocalSearchType.HOTEL);
    }else if(searchType.compareTo("HOSPITAL") == 0){
      kakaoLocalRequest.setSearchType(LocalSearchType.HOSPITAL);
    }else{
      kakaoLocalRequest.setSearchType(LocalSearchType.ETC);
    }
    Page<KakaoPlaceDTO> kakaoPlaceDTOS = kakaoLocalServiceInterface
        .getLocalService(kakaoLocalRequest);
    if(kakaoPlaceDTOS.getTotalElements()>0){
      result.put("result", true);
      result.put("message", "추천 서비스 목록입니다.");
      result.put("recommendList", kakaoPlaceDTOS);
      result.put("totalPages", kakaoPlaceDTOS.getTotalPages());
      result.put("currentPage", kakaoPlaceDTOS.getNumber());
    }else{
      result.put("result", false);
      result.put("message","추천 목록이 존재하지 않습니다. 주변에 사업체가 존재하지 않거나, api 연결 문제일수도 있습니다!");
    }

    return ResponseEntity.ok(result);
  }
}
