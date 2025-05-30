package com.petservice.main.common.database.dto;

import lombok.Data;

@Data
public class CatBreedDTO {
  private String id;
  private String name;
  private String origin;
  private String temperament;
  private String lifeSpan;
}
