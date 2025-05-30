package com.petservice.main.common.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cat_breed")
@Getter
@Setter
public class CatBreed {

  @Id
  private String id;
  private String name;
  private String origin;
  private String temperament;
  private String lifeSpan;

}
