package com.petservice.main.business.database.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class NaverApiResponse {

  @JsonProperty("lastBuildDate")
  private String lastBuildDate;

  /** 총 검색 결과 건수 */
  @JsonProperty("total")
  private int total;

  /** 요청한 검색 시작 위치 (1부터 시작) */
  @JsonProperty("start")
  private int start;

  /** 한 번에 보여진 검색 결과 개수 */
  @JsonProperty("display")
  private int display;

  /** 실제 검색 결과 아이템 리스트 */
  @JsonProperty("items")
  private List<NaverPlaceItem> items;
}
