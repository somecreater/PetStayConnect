package com.petservice.main.business.database.entity;

import com.petservice.main.user.database.entity.Pet;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pet_reservation")
@Getter
@Setter
public class PetReservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "pet_id", nullable = false)
  public Pet pet;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "reservation_id", nullable = false)
  private Reservation reservation;
}
