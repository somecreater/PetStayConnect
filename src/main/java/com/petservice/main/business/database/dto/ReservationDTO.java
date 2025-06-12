package com.petservice.main.business.database.dto;


import com.petservice.main.business.database.entity.ReservationStatus;
import com.petservice.main.payment.database.dto.PaymentDTO;
import com.petservice.main.review.database.dto.ReviewDTO;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ReservationDTO {
    private Long id;
    private Long userId;
    private Long petBusinessId;
    private Long petBusinessRoomId;

    private String roomType;

    private LocalDate checkIn;
    private LocalDate checkOut;

    private String specialRequests;
    private String businessRequestInfo;

    private Integer period;

    private ReservationStatus status;

    private PaymentDTO paymentDTO;
    private List<PetReservationDTO> PetReservationDTOList = new ArrayList<>();
    private ReviewDTO reviewDTO;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //추가 정보
    private String petBusinessName;
    private String petBusinessRegisterNumber;
    private String userLoginId;
}
