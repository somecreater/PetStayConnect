package com.petservice.main.review.database.repository;

import com.petservice.main.review.database.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    boolean existsByReservationId(Long reservationId);
    List<Review> findAllByUser_UserLoginId(String userLoginId);
}
