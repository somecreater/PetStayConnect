package com.petservice.main.review.database.mapper;

import com.petservice.main.review.database.dto.ReviewDTO;
import com.petservice.main.review.database.entity.ReviewEntity;
import com.petservice.main.user.database.repository.UserRepository;
//import com.petservice.main.reservation.database.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final UserRepository userRepository;
//    private final ReservationRepository reservationRepository;

    // DTO → Entity
    public ReviewEntity toEntity(ReviewDTO dto) {
        ReviewEntity entity = new ReviewEntity();
        entity.setId(dto.getId());
        entity.setRating(dto.getRating());
        entity.setContent(dto.getContent());
        entity.setReportCount(dto.getReportCount());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setPetBusinessName(dto.getPetBusinessName());
        entity.setPetBusinessLocation(dto.getPetBusinessLocation());

        // FK: user
        if (dto.getUserId() != null) {
            entity.setUser(userRepository.findById(dto.getUserId()).orElse(null));
        }
        // FK: reservation
//        if (dto.getReservationId() != null) {
//            entity.setReservation(reservationRepository.findById(dto.getReservationId()).orElse(null));
//        }
        return entity;
    }

    // Entity → DTO
    public ReviewDTO toDTO(ReviewEntity entity) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(entity.getId());
        dto.setRating(entity.getRating());
        dto.setContent(entity.getContent());
        dto.setReportCount(entity.getReportCount());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setPetBusinessName(entity.getPetBusinessName());
        dto.setPetBusinessLocation(entity.getPetBusinessLocation());

        // FK: user
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
//        // FK: reservation
//        if (entity.getReservation() != null) {
//            dto.setReservationId(entity.getReservation().getId());
//        }
        return dto;
    }
}