package com.petservice.main.api.database.dto;

import lombok.Data;

@Data
public class NaverSearchRequest {
  private boolean near;
  private String businessName;
  private String sectorCode;
  private String typeCode;
}
