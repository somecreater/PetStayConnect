package com.petservice.main.review.database.repository;

import com.petservice.main.review.database.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
}