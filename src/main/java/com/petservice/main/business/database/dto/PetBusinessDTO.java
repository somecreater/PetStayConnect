package com.petservice.main.business.database.dto;

import com.petservice.main.business.database.entity.BusinessStatus;
import com.petservice.main.business.database.entity.Varification;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PetBusinessDTO {

  private Long id;
  private String businessName;
  private BusinessStatus status;
  private Integer minPrice;
  private Integer maxPrice;
  private String facilities;
  private String description;
  private Integer avgRate;
  private String registrationNumber;
  private String bankAccount;
  private Varification varification;
  private Long userId;
  private String petBusinessTypeName;
  private Long petBusinessTypeId;
  private List<PetBusinessRoomDTO> petBusinessRoomDTOList=new ArrayList<>();
  private List<ReservationDTO> reservationDTOList=new ArrayList<>();
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
