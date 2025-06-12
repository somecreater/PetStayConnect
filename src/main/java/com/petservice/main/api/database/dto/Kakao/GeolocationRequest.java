package com.petservice.main.api.database.dto.Kakao;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GeolocationRequest {
  private boolean considerIp;
}
