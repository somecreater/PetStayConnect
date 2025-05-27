package com.petservice.main.review.controller;

import com.petservice.main.review.database.dto.ReviewDTO;
import com.petservice.main.review.service.ReviewService;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;



    @GetMapping("/my")
    public ResponseEntity<List<ReviewDTO>> getMyReviews(@AuthenticationPrincipal CustomUserDetails principal) {
        String userLoginId = principal.getUsername();
        List<ReviewDTO> reviews = reviewService.getMyReviews(userLoginId);
        return ResponseEntity.ok(reviews);

    }

//    @GetMapping("/{reviewId}")
//    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long reviewId,
//                                               @AuthenticationPrincipal CustomUserDetails principal) {
//
//        String userLoginId = principal.getUsername();
//        ReviewDTO review = reviewService.getReviewById(reviewId, userLoginId);
//        return ResponseEntity.ok(review);
//    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> listAllReviews() {
        List<ReviewDTO> all = reviewService.getAllReviews();
        return ResponseEntity.ok(all);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO dto,
                                                  @AuthenticationPrincipal CustomUserDetails principal) {
        String userLoginId = principal.getUsername();
        ReviewDTO created = reviewService.createReview(userLoginId, dto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long reviewId,
                                                  @RequestBody ReviewDTO dto,
                                                  @AuthenticationPrincipal CustomUserDetails principal) {
        String userLoginId = principal.getUsername();
        dto.setId(reviewId);
        ReviewDTO updated = reviewService.updateReview(userLoginId, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId,
                                               @AuthenticationPrincipal CustomUserDetails principal) {

        String userLoginId = principal.getUsername();
        log.info("🗑️ DELETE /api/reviews/{} 요청 by {}", reviewId, userLoginId);
        reviewService.deleteReview(reviewId, userLoginId);
        return ResponseEntity.ok("리뷰가 삭제되었습니다.");
    }

}




