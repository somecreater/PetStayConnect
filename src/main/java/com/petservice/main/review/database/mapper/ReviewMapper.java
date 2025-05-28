package com.petservice.main.review.database.mapper;

import com.petservice.main.business.database.entity.PetBusiness;
import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.business.database.repository.ReservationRepository;
import com.petservice.main.review.database.dto.ReviewDTO;
import com.petservice.main.review.database.entity.Review;
import com.petservice.main.user.database.repository.UserRepository;
//import com.petservice.main.reservation.database.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ReviewMapper {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    public Review toEntity(ReviewDTO dto) {
        Review entity = new Review();
        entity.setId(dto.getId());
        entity.setRating(dto.getRating());
        entity.setContent(dto.getContent());
        entity.setReportCount(dto.getReportCount());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setPetBusinessName(dto.getPetBusinessName());
        entity.setPetBusinessLocation(dto.getPetBusinessLocation());

        if (dto.getUserId() != null) {
            entity.setUser(userRepository.findById(dto.getUserId()).orElse(null));
        }
        if (dto.getReservationId() != null) {
            entity.setReservation(reservationRepository.findById(dto.getReservationId()).orElse(null));
        }
        return entity;
    }

    public ReviewDTO toDTO(Review entity) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(entity.getId());
        if(entity.getUser()!=null) {
            dto.setUserLoginId(entity.getUser().getUserLoginId());
        }
        dto.setRating(entity.getRating());
        dto.setContent(entity.getContent());
        dto.setReportCount(entity.getReportCount());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setPetBusinessName(entity.getPetBusinessName());
        dto.setPetBusinessLocation(entity.getPetBusinessLocation());

        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
//         FK: reservation
        if (entity.getReservation() != null) {
            Reservation res = entity.getReservation();
            dto.setReservationId(res.getId());


            PetBusiness pb = entity.getReservation().getPetBusiness();
            if (pb != null) {
              dto.setPetBusinessName(pb.getBusinessName());
              dto.setPetBusinessLocation(pb.getCity() + " " + pb.getTown());

           }
        }
        return dto;
    }

    public ReviewDTO toBasicDTO(Review entity){
        ReviewDTO dto = new ReviewDTO();
        dto.setId(entity.getId());
        dto.setRating(entity.getRating());
        dto.setContent(entity.getContent());
        dto.setReportCount(entity.getReportCount());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setPetBusinessName(entity.getPetBusinessName());
        dto.setPetBusinessLocation(entity.getPetBusinessLocation());

        return dto;
    }
}
