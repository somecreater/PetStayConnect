package com.petservice.main.review.service;

import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.business.database.entity.ReservationStatus;
import com.petservice.main.business.database.repository.ReservationRepository;
import com.petservice.main.review.database.dto.ReviewDTO;
import com.petservice.main.review.database.entity.Review;
import com.petservice.main.review.database.mapper.ReviewMapper;
import com.petservice.main.review.database.repository.ReviewRepository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewMapper reviewMapper;

    @Override
    @Transactional
    public ReviewDTO createReview(String userLoginId, ReviewDTO dto) {

        Reservation reservation = reservationRepository.findById(dto.getReservationId())
                .orElseThrow(() -> new IllegalArgumentException("예약 정보가 없습니다."));


        if (!reservation.getUser().getUserLoginId().equals(userLoginId)) {
            throw new AccessDeniedException("본인의 예약만 리뷰 작성이 가능합니다.");
        }

        if (reservation.getStatus() != ReservationStatus.COMPLETED) {
            throw new IllegalStateException("완료된 예약만 리뷰 작성 가능합니다.");
        }

        if (reviewRepository.existsByReservationId(dto.getReservationId())) {
            throw new IllegalStateException("이미 리뷰가 작성된 예약입니다.");
        }

        Review review = reviewMapper.toEntity(dto);
        review.setReservation(reservation);
        review.setUser(reservation.getUser());
        review.setReportCount(0);

        Review saved = reviewRepository.save(review);
        return reviewMapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getReviewById(Long reviewId, String userLoginId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        if (!review.getUser().getUserLoginId().equals(userLoginId)) {
            throw new AccessDeniedException(("본인의 리뷰만 조회할 수 있습니다."));
        }

        return reviewMapper.toDTO(review);
    }


//    @Override
//    @Transactional(readOnly = true)
//    public List<ReviewDTO> getMyReviews(String userLoginId) {
//        return reviewRepository.findAllByUser_UserLoginId(userLoginId).stream()
//                .map(reviewMapper::toDTO)
//                .toList();
//    }

    @Override
    @Transactional
    public ReviewDTO updateReview(String userLoginId, ReviewDTO dto) {
        Review review = reviewRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));

        if (!review.getUser().getUserLoginId().equals(userLoginId)) {
            throw new AccessDeniedException("본인의 리뷰만 수정할 수 있습니다.");
        }

        review.setRating(dto.getRating());
        review.setContent(dto.getContent());

        Review updated = reviewRepository.save(review);
        return reviewMapper.toBasicDTO(updated);

    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll()
                .stream()
                .map(reviewMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public void deleteReview(Long reviewId, String userLoginId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 존재하지 않습니다."));


        if (!review.getUser().getUserLoginId().equals(userLoginId)) {
            throw new AccessDeniedException("본인의 리뷰만 삭제할 수 있습니다.");
        }

        // --- 연관관계 끊기(주인 아닌 쪽: Reservation.review) ---
        Reservation reservation = review.getReservation();
        if (reservation != null) {
            reservation.setReview(null);
            reservationRepository.save(reservation);
        }


        reviewRepository.delete(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable).map(reviewMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> getMyReviews(String userLoginId, Pageable pageable) {
        return reviewRepository.findAllByUser_UserLoginId(userLoginId, pageable)
                .map(reviewMapper::toDTO);
    }

}

