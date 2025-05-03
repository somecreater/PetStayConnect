package com.petservice.main.business.database.dto;

import lombok.Data;

@Data
public class SearchRequest {
  private String businessName;
  private String sectorCode;
  private String typeCode;
  private boolean is_around;
}
