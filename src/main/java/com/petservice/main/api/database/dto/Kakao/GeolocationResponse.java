package com.petservice.main.api.database.dto.Kakao;

import lombok.Data;

@Data
public class GeolocationResponse {
  private LocationDTO location;
  private double accuracy;
}
