package com.petservice.main.common.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "dog_breed")
@Getter
@Setter
public class DogBreed {

  @Id
  private Integer id;
  private String name;
  private String temperament;
  private String lifeSpan;
}
