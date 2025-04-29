package com.petservice.main.pet.database.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "pet")
@Getter @Setter
public class Pet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private String healthInfo;
    private String gender;

    @Column(name = "user_id")
    private Long userId;
}
