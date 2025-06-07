package com.petservice.main.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petservice.main.api.database.dto.Naver.NaverApiResponse;
import com.petservice.main.api.database.dto.Naver.NaverPlaceItem;
import com.petservice.main.api.database.dto.Naver.NaverSearchRequest;
import com.petservice.main.business.database.dto.*;
import com.petservice.main.business.database.entity.BusinessStatus;
import com.petservice.main.business.database.entity.Varification;
import com.petservice.main.api.service.Interface.NaverSearchServiceInterface;
import com.petservice.main.business.service.Interface.PetBusinessTypeServiceInterface;
import com.petservice.main.user.database.dto.AddressDTO;
import com.petservice.main.user.database.mapper.AddressMapper;
import com.petservice.main.user.service.Interface.AddressServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverSearchService implements NaverSearchServiceInterface {

  private final PetBusinessTypeServiceInterface petBusinessTypeService;
  private final AddressServiceInterface addressService;

  @Value("${naver.api.base-url}") private String baseUrl;
  @Value("${naver.api.client-id}") private String clientId;
  @Value("${naver.api.client-secret}") private String clientSecret;

  // 애완동물 관련 카테고리 그룹 코드
  private static final List<String> PET_CATEGORY_CODES = Arrays.asList(
      "CE7", // 애완 동물 소매
      "HP8", // 동물병원 운영
      "PD3", "OD5" // 펫 미용·목욕 서비스
      // 필요 시 추가
  );

  private final AddressMapper addressMapper;
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public Page<PetBusinessDTO> searchNearPyBusinessDTO(
      String userLoginId, NaverSearchRequest request, Pageable pageable){
    try {
      AddressDTO address = addressService.getAddressByUserLoginId(userLoginId);

      String searchKeyword = null;
      if(StringUtils.hasText(request.getBusinessName())
          && StringUtils.hasText(request.getTypeCode())){
        searchKeyword = request.getTypeCode() + " " + request.getBusinessName();
      }else if(StringUtils.hasText(request.getBusinessName())){
        searchKeyword = request.getBusinessName();
      }else if(StringUtils.hasText(request.getTypeCode())){
        searchKeyword = request.getTypeCode();
      }
      if (!StringUtils.hasText(searchKeyword)) {
        throw new IllegalArgumentException("businessName 또는 typeCode 중 하나는 필수입니다.");
      }

      String query = searchKeyword;
      int start = pageable.getPageNumber() * pageable.getPageSize() + 1;
      int display = pageable.getPageSize();
      log.info("페이징 처리: {}   {}", start, display);
      NaverApiResponse apiResponse = null;

      UriComponentsBuilder uriBuilder= UriComponentsBuilder
          .fromUriString(baseUrl)
          .queryParam("query",query)
          .queryParam("display", display)
          .queryParam("start",start)
          .queryParam("sort","random")
          .queryParam("category_group_code", String.join(",", PET_CATEGORY_CODES));

      if(request.isNear() && address!=null){
        uriBuilder.queryParam("x",address.getCorX()).queryParam("y",address.getCorY());
      }

      URI uri= uriBuilder.build(false).encode().toUri();
      log.info("NAVER URI = {}", uri.toString());
      WebClient webClient= WebClient.builder()
          .defaultHeader("X-Naver-Client-Id", clientId)
          .defaultHeader("X-Naver-Client-Secret", clientSecret)
          .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8")
          .defaultHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.name())
          .build();

      ResponseEntity<String> responseEntity = webClient.get()
          .uri(uri)
          .retrieve()
          .toEntity(String.class)
          .block();
      String resultBody = responseEntity.getBody();

      log.info("Serialized JSON: {}", resultBody);
      apiResponse = objectMapper.readValue(resultBody, NaverApiResponse.class);

      // 3) 빈 결과 처리
      if (apiResponse.getItems().isEmpty()) {
        log.info("결과가 존재하지 않습니다");
        return new PageImpl<>(List.of(), pageable, apiResponse.getTotal());
      }
      for(NaverPlaceItem test: apiResponse.getItems()){
        log.info("{}   {}   {}   {}   {}   {}   {}   {}   {}   {}   {}",
            test.getTitle(),test.getLink(),test.getCategory(),test.getCategoryGroupName(),
            test.getCategoryGroupCode(),test.getDescription(),test.getTelephone(),
            test.getAddress(), test.getRoadAddress(), test.getMapX(), test.getMapY());
      }

      List<PetBusinessDTO> filtered = apiResponse.getItems().stream()
        .filter(item -> {

          if(item.getCategory() != null){
            if(item.getCategory().contains(">")) {
              String[] parts = item.getCategory().split(">");
              item.setCategoryGroupName(parts[parts.length - 1]);
            }else{
              item.setCategoryGroupName(item.getCategory());
            }
          }else{
            item.setCategoryGroupName("");
          }
          return true;

      }).map(naverPlaceItem -> {
        String rawTitle = naverPlaceItem.getTitle();
        String title = stripHtmlTags(rawTitle).toLowerCase();
        naverPlaceItem.setTitle(title);
        PetBusinessDTO dto = ConvertBusinessDTO(naverPlaceItem, address);
        PetBusinessTypeDTO typeDto = ConvertBusinessTypeDTO(naverPlaceItem.getCategoryGroupCode(),
            naverPlaceItem.getCategoryGroupName());
        dto.setPetBusinessTypeId(typeDto.getId());
        dto.setPetBusinessTypeName(typeDto.getTypeName());
        return dto;

      }).toList();

      return new PageImpl<>(filtered, pageable, apiResponse.getTotal());
    }catch (Exception e){
      log.error("Naver API 호출 중 오류", e);
      throw new RuntimeException("검색 중 오류가 발생했습니다.", e);
    }
  }

  //Naver Map api를 통해 조회된 애완동물 서비스 관련 사업체 정보를 PetBusinessDTO로 전환
  @Override
  public PetBusinessDTO ConvertBusinessDTO(NaverPlaceItem placeItem, AddressDTO addressDTO){
    PetBusinessDTO dto = new PetBusinessDTO();
    dto.setBusinessName(placeItem.getTitle());
    dto.setDescription(placeItem.getDescription());
    dto.setRegistrationNumber(placeItem.getTelephone() + "(전화번호)");
    dto.setDescription(dto.getDescription()
        + " | 주소: " + placeItem.getRoadAddress());

    if(addressDTO !=null && placeItem.getMapX() !=null && placeItem.getMapY() !=null){
      double distKm=getDistance(addressDTO,
          Double.parseDouble(placeItem.getMapX()),
          Double.parseDouble(placeItem.getMapY()));
      dto.setDescription(dto.getDescription()
          + String.format(" (회원 님과의 거리: %.2fkm)", distKm));
    }
    dto.setPetBusinessTypeId(null);
    dto.setPetBusinessTypeName(placeItem.getCategoryGroupName());

    dto.setAvgRate(0);
    dto.setFacilities("");
    dto.setBankAccount("");
    dto.setVarification(Varification.NONE);
    dto.setStatus(BusinessStatus.OPERATION);

    return dto;
  }

  // 카테고리 내의 이름을 통해 해당 사업체의 종류를 db 에서 검색()
  @Override
  public PetBusinessTypeDTO ConvertBusinessTypeDTO(String categoryGroupCode, String categoryGroupName){

    if (!StringUtils.hasText(categoryGroupCode) && !StringUtils.hasText(categoryGroupName)) {
      return petBusinessTypeService.getTypeByTypeCode("기타 애완동물 개인 서비스");
    }

    categoryGroupCode = categoryGroupCode != null ? categoryGroupCode.trim().toUpperCase() : "";
    categoryGroupName = categoryGroupName != null ? categoryGroupName.trim().toLowerCase() : "";
    if ("CE7".equals(categoryGroupCode)) {
      return petBusinessTypeService.getTypeByTypeCode("애완 동물 소매");
    } else if ("HP8".equals(categoryGroupCode)) {
      return petBusinessTypeService.getTypeByTypeCode("동물병원 운영");
    } else if ("PD3".equals(categoryGroupCode) || "OD5".equals(categoryGroupCode)) {
      return petBusinessTypeService.getTypeByTypeCode("펫 미용·목욕 서비스");
    }

    if (categoryGroupName.contains("용품")
        || categoryGroupName.contains("소매")) {
      return petBusinessTypeService.getTypeByTypeCode("애완 동물 소매");
    }
    if (categoryGroupName.contains("사료")) {
      return petBusinessTypeService.getTypeByTypeCode("애완 동물 사료 소매");
    }
    if (categoryGroupName.contains("병원")) {
      return petBusinessTypeService.getTypeByTypeCode("동물병원 운영");
    }
    if (categoryGroupName.contains("진료")) {
      return petBusinessTypeService.getTypeByTypeCode("수의 진료 서비스");
    }
    if (categoryGroupName.contains("장묘")
        || categoryGroupName.contains("장례")
        || categoryGroupName.contains("화장")) {
      return petBusinessTypeService.getTypeByTypeCode("애완동물 장례식장");
    }
    if (categoryGroupName.contains("위탁")
        || (categoryGroupName.contains("돌봄")
        && categoryGroupName.contains("펫"))) {
      return petBusinessTypeService.getTypeByTypeCode("펫시터·장기위탁 돌봄");
    }
    if (categoryGroupName.contains("호텔")
        || categoryGroupName.contains("데이케어")
        || categoryGroupName.contains("숙박")) {
      return petBusinessTypeService.getTypeByTypeCode("반려동물 호텔·데이케어");
    }
    if (categoryGroupName.contains("미용")
        || categoryGroupName.contains("목욕")
        || categoryGroupName.contains("그루밍")) {
      return petBusinessTypeService.getTypeByTypeCode("펫 미용·목욕 서비스");
    }
    if (categoryGroupName.contains("훈련")
        || categoryGroupName.contains("교정")) {
      return petBusinessTypeService.getTypeByTypeCode("애견훈련소");
    }
    if (categoryGroupName.contains("보호")
        || categoryGroupName.contains("센터")) {
      return petBusinessTypeService.getTypeByTypeCode("유기견 보호센터 운영");
    }

    return petBusinessTypeService.getTypeByTypeCode("기타 애완동물 개인 서비스");
  }

  //좌표 값을 활용해서 db에 저장된 회원의 위치와 사업체의 실제 위치 사이의 거리 계산
  @Override
  public double getDistance(AddressDTO addressDTO, double corX, double corY){
    double userLat = addressDTO.getCorY();
    double userLon = addressDTO.getCorX();

    double bizLat  = corY;
    double bizLon  = corX;

    double R = 6371; // 지구 반경(km)
    double dLat = Math.toRadians(bizLat - userLat);
    double dLon = Math.toRadians(bizLon - userLon);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
        + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(bizLat))
        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return R * c;
  }

  private String stripHtmlTags(String html) {
    return html.replaceAll("<[^>]*>", "");
  }
}
