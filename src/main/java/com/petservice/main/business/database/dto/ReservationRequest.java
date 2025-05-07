package com.petservice.main.business.database.dto;

import com.petservice.main.user.database.dto.PetDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//예약 등록을 위한 DTO(아직 미결재 상태)
@Data
public class ReservationRequest {
  private String user_login_id;
  private String business_register_number;
  private String roomType;
  private LocalDate checkIn;
  private LocalDate checkOut;
  private String specialRequests;
  private String businessRequestInfo;
  private List<PetDTO> petDTOList;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
