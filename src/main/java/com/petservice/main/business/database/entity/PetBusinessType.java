package com.petservice.main.business.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "pet_business_type")
@Entity
@Getter
@Setter
public class PetBusinessType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "type_name", unique = true)
  private String typeName;

  @Column(name="sector_code")
  private String sectorCode;

  @Column(name="type_code")
  private String typeCode;

  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "petBusinessType")
  private List<PetBusiness> petBusinessList = new ArrayList<>();
}
