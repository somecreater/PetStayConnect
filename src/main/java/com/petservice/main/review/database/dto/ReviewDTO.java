package com.petservice.main.review.database.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private Long reservationId;
    private Long userId;
    private Integer rating;
    private String content;
    private Integer reportCount;
    private String petBusinessName;
    private String petBusinessLocation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
