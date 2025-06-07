package com.petservice.main.api.database.dto.Kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Document {
  @JsonProperty("place_name")
  private String placeName;
  @JsonProperty("address_name")
  private String addressName;
  @JsonProperty("road_address_name")
  private String roadAddressName;
  private String phone;
  private String distance;
}
