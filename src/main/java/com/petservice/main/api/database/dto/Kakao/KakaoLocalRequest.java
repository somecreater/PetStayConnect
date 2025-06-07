package com.petservice.main.api.database.dto.Kakao;

import lombok.Data;

@Data
public class KakaoLocalRequest {
  private Integer page;
  private Integer size;
  private Integer distance;
  private Double latitude;
  private Double longitude;
  private LocalSearchType searchType;
}
