package com.petservice.main.user.database.dto;

import com.petservice.main.user.database.entity.Gender;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PetDTO {

  private Long id;
  private String name;
  private String species;
  private String breed;
  private LocalDate birthDate;
  private Integer age;
  private String healthInfo;
  private Gender gender;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
  private Long userId;
  private String userLoginId;
}
