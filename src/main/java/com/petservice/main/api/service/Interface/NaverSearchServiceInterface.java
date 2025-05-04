package com.petservice.main.api.service.Interface;

import com.petservice.main.api.database.dto.NaverPlaceItem;
import com.petservice.main.api.database.dto.NaverSearchRequest;
import com.petservice.main.business.database.dto.PetBusinessDTO;
import com.petservice.main.business.database.dto.PetBusinessTypeDTO;
import com.petservice.main.user.database.dto.AddressDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NaverSearchServiceInterface {

  //Naver Search api로 전체 or 회원 주변의 사업체 정보 가져오기
  public Page<PetBusinessDTO> searchNearPyBusinessDTO(String userLoginId,
      NaverSearchRequest naverSearchRequest, Pageable pageable);

  //Naver Search api를 통해 조회된 애완동물 서비스 관련 사업체 정보를 PetBusinessDTO로 전환
  public PetBusinessDTO ConvertBusinessDTO(NaverPlaceItem placeItem, AddressDTO addressDTO);

  //Naver Search api를 통해 조회된 애완동물 서비스 관련 사업체 정보에서 타입을 매핑
  public PetBusinessTypeDTO ConvertBusinessTypeDTO(String categoryGroupCode, String categoryGroupName);

  //좌표 값을 활용해서 db에 저장된 회원의 위치와 사업체의 실제 위치 사이의 거리 계산
  public double getDistance(AddressDTO addressDTO, double corX, double corY);
}
