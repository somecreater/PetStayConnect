package com.petservice.main.business.database.dto;


import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ReservationDTO {
    private Long id;
    private Long petBusinessId;
    private Long userId;
    private Long petBusinessRoomId;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private String specialRequests;
    private String businessReservationInfo;

    private Integer period;

    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
