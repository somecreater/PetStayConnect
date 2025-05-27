package com.petservice.main.review.service;

import com.petservice.main.review.database.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    ReviewDTO createReview(String userLoginId, ReviewDTO dto);

//    ReviewDTO getReviewById(Long reviewID, String userLoginId);

    List<ReviewDTO> getMyReviews(String userLoginId);

    ReviewDTO updateReview(String userLoginId, ReviewDTO dto);

    List<ReviewDTO> getAllReviews();

    void deleteReview(Long reviewId, String userLoginId);

    Page<ReviewDTO> getAllReviews(Pageable pageable);

    Page<ReviewDTO> getMyReviews(String userLoginId, Pageable pageable);

}
