package com.petservice.main.business.database.entity;

import com.petservice.main.common.database.entity.TimeEntity;
import com.petservice.main.user.database.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;

import java.util.ArrayList;
import java.util.List;

@Table(name = "pet_business")
@Entity
@Getter
@Setter
public class PetBusiness extends TimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "business_name", nullable = false)
  private String businessName;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private BusinessStatus status;

  @Column(name = "min_price", nullable = false)
  private Integer minPrice = 50000;

  @Column(name = "max_price", nullable = false)
  private Integer maxPrice = 100000;

  @Column(columnDefinition = "TEXT")
  private String facilities;

  @Column(columnDefinition = "TEXT")
  private String description;

  @Column(name = "avg_rate")
  private Integer avgRate;

  @Column(name = "registration_number", nullable = false, unique = true)
  private String registrationNumber;

  @Column(name = "bank_account", unique = true)
  private String bankAccount;

  @Enumerated(EnumType.STRING)
  @Column(name = "varification_status")
  private Varification varification;

  @Column
  private String province;

  @Column
  private String city;

  @Column
  private String town;

  @OneToOne(fetch = FetchType.LAZY, optional = true)
  @JoinColumn(name = "user_id", nullable = true)
  private User user;

  @ManyToOne
  @JoinColumn(name = "business_type_id")
  private PetBusinessType petBusinessType;

  @OneToMany(mappedBy = "petBusiness", fetch = FetchType.LAZY)
  private List<PetBusinessRoom> petBusinessRoomList=new ArrayList<>();

  @OneToMany(mappedBy = "petBusiness", fetch = FetchType.LAZY)
  private List<Reservation> reservationList=new ArrayList<>();

}
