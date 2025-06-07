package com.petservice.main.api.database.dto.Naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NaverPlaceItem {
  /** 사업체명 (HTML 태그 포함) */
  private String title;

  /** 검색 결과 링크 (상세 정보 페이지) */
  private String link;

  /** 원본 카테고리 문자열, ex. "반려동물>반려동물미용" */
  @JsonProperty("category")
  private String category;

  /** 카테고리 그룹명 (예: "반려동물미용") */
  @JsonProperty("category_group_name")
  private String categoryGroupName;

  /** 카테고리 그룹 코드, ex. "PD3" 등 */
  @JsonProperty("category_group_code")
  private String categoryGroupCode;

  /** 설명 */
  private String description;

  /** 전화번호 */
  private String telephone;

  /** 지번 주소 */
  private String address;

  /** 도로명 주소 */
  @JsonProperty("roadAddress")
  private String roadAddress;

  /** 지도 X 좌표 (경도) */
  @JsonProperty("mapx")
  private String mapX;

  /** 지도 Y 좌표 (위도) */
  @JsonProperty("mapy")
  private String mapY;
}
