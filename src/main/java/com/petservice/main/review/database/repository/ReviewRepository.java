package com.petservice.main.review.database.repository;

import com.petservice.main.review.database.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByReservationId(Long reservationId);
    List<Review> findAllByUser_UserLoginId(String userLoginId);
    List<Review> findAllByReservation_PetBusiness_Id(Long businessId);
    Page<Review> findAll(Pageable pageable);
    Page<Review> findAllByUser_UserLoginId(String userLoginId, Pageable pageable);
    @EntityGraph(attributePaths = {"reservation", "reservation.petBusiness"})
    Optional<Review> findById(Long id);
}
