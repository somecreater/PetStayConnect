package com.petservice.main.business.database.dto;

import com.petservice.main.business.database.entity.BusinessStatus;
import com.petservice.main.business.database.entity.Varification;
import lombok.Data;

@Data
public class PetBusinessDTO {

  private Long id;
  private String businessName;
  private BusinessStatus status;
  private Integer minPrice;
  private Integer maxPrice;
  private String facilities;
  private String description;
  private Integer avgRate;
  private String registrationNumber;
  private String bankAccount;
  private Varification varification;
  private Long userId;
  private Long petBusinessTypeId;
}
