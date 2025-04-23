package com.petservice.main.business.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pet_business_room")
@Getter
@Setter
public class PetBusinessRoom {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "room_type")
  private String roomType;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "room_count")
  private Integer roomCount;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pet_business_id")
  private PetBusiness petBusiness;

  @OneToMany(mappedBy = "petBusinessRoom", fetch = FetchType.LAZY)
  private List<Reservation> reservationList=new ArrayList<>();

}
