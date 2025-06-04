package com.petservice.main.business.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name = "pet_business_tag")
@Entity
@Getter
@Setter
public class PetBusinessTag {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tag_name")
  private String tagName;

  @Enumerated(EnumType.STRING)
  @Column(name = "tag_type")
  private TagType tagType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pet_business_id")
  private PetBusiness petBusiness;
}
