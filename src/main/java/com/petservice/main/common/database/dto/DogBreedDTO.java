package com.petservice.main.common.database.dto;

import lombok.Data;

@Data
public class DogBreedDTO {
  private Integer id;
  private String name;
  private String temperament;
  private String lifeSpan;
}
