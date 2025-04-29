package com.petservice.main.pet.database.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class PetDTO {
    private Long id;
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private String healthInfo;
    private String gender;
    private Long userId;
}