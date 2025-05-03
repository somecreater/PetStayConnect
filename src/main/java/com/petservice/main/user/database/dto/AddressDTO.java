package com.petservice.main.user.database.dto;

import lombok.Data;

@Data
public class AddressDTO {
  private Long id;
  private Long userId;
  private String userLoginId;
  private Double corX;
  private Double corY;
  private String roadAddress;
  private String detailAddress;
  private String postalAddress;
  private String province;
  private String city;
  private String town;
}
