package com.petservice.main.api.service;

import com.petservice.main.api.database.dto.Kakao.*;
import com.petservice.main.api.service.Interface.KakaoLocalServiceInterface;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoLocalService implements KakaoLocalServiceInterface {

  @Value("${kakao.api-key}")
  private String KakaoApiKey;
  @Value("${kakao.search-url}")
  private String KakaoSearchUrl;
  @Value("${google.api-key}")
  private String googleApiKey;
  @Value("${google.geolocate-url}")
  private String geolocateUrl;

  @Override
  public GeolocationResponse geolocate(){
    WebClient client= WebClient.builder()
        .baseUrl(geolocateUrl)
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .build();

    GeolocationRequest requestBody = new GeolocationRequest(true);

    GeolocationResponse resp = client.post()
        .uri(uriBuilder -> uriBuilder
            .queryParam("key", googleApiKey)
            .build()
        )
        .bodyValue(requestBody)
        .retrieve()
        .bodyToMono(GeolocationResponse.class)
        .block();

    if (resp == null || resp.getLocation() == null) {
      return null;
    }
    return resp;
  }

  @Override
  public Page<KakaoPlaceDTO> getLocalService(KakaoLocalRequest request) {

    String query="";

    if(request.getSearchType() == LocalSearchType.HOTEL){
      query="애완동물 호텔";
    }else if(request.getSearchType() == LocalSearchType.HOSPITAL){
      query="애완동물 병원";
    }else{
      throw new IllegalArgumentException("로컬 검색 타입이 부적절 합니다!!");
    }

    UriComponentsBuilder uriBuilder= UriComponentsBuilder
        .fromUriString(KakaoSearchUrl)
        .queryParam("query",query)
        .queryParam("y", request.getLatitude())
        .queryParam("x", request.getLongitude())
        .queryParam("radius", request.getDistance())
        .queryParam("sort", "distance")
        .queryParam("page", request.getPage()+1)
        .queryParam("size", request.getSize());

    URI uri= uriBuilder.build(false).encode().toUri();

    WebClient webClient = WebClient.builder()
        .defaultHeader("Authorization", "KakaoAK " + KakaoApiKey)
        .build();

    KakaoResponse response = webClient.get()
        .uri(uri)
        .retrieve()
        .bodyToMono(KakaoResponse.class)
        .block();

    List<KakaoPlaceDTO> list = response.getDocuments().stream()
        .map(document -> new KakaoPlaceDTO(
            document.getPlaceName(),
            document.getRoadAddressName() != null
              ? document.getRoadAddressName(): document.getAddressName(),
            document.getPhone(),
            Integer.parseInt(document.getDistance())
        )).toList();

    return new PageImpl<>(
        list,
        Pageable.ofSize(request.getSize()).withPage(request.getPage()),
        response.getMeta().getTotalCount()
    );
  }
}
