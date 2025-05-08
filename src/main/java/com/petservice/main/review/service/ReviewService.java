package com.petservice.main.review.service;

import com.petservice.main.review.database.dto.ReviewDTO;

import java.util.List;

public interface ReviewService {
    ReviewDTO createReview(String userLoginId, ReviewDTO dto);

    ReviewDTO getReviewById(Long reviewID, String userLoginId);

    List<ReviewDTO> getMyReviews(String userLoginId);

    ReviewDTO updateReview(String userLoginId, ReviewDTO dto);

    void deleteReview(Long reviewId, String userLoginId);

}
