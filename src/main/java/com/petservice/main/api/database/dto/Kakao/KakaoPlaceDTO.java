package com.petservice.main.api.database.dto.Kakao;

import lombok.Data;

@Data
public class KakaoPlaceDTO {
  private String placeName;
  private String address;
  private String phone;
  private int distance;

  public KakaoPlaceDTO(String placeName, String address, String phone, int distance) {
    this.placeName=placeName;
    this.address=address;
    this.phone=phone;
    this.distance=distance;
  }
}
