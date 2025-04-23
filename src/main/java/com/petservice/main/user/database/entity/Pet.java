package com.petservice.main.user.database.entity;

import com.petservice.main.common.database.entity.TimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Table(name = "pet")
@Entity
@Getter
@Setter
public class Pet extends TimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String name;

  @Column
  private String species;

  @Column
  private String breed;

  @Column(name = "birth_date")
  private LocalDate birthDate;

  @Column(name = "health_info", columnDefinition = "TEXT")
  private String healthInfo;

  @Enumerated(EnumType.STRING)
  @Column
  private Gender gender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
