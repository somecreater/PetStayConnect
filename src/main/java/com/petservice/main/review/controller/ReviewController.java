package com.petservice.main.review.controller;

import com.petservice.main.review.database.dto.ReviewDTO;
import com.petservice.main.review.service.ReviewService;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
    public ResponseEntity<Page<ReviewDTO>> listMy(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<ReviewDTO> page = reviewService.getMyReviews(user.getUsername(), pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long reviewId) {

        ReviewDTO review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    @GetMapping
    public ResponseEntity<Page<ReviewDTO>> listAll(
        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
        Pageable pageable
            ) {
                Page<ReviewDTO> page = reviewService.getAllReviews(pageable);
                return ResponseEntity.ok(page);
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




