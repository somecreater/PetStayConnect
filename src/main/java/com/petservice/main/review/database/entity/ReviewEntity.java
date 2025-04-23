package com.petservice.main.review.database.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.petservice.main.user.database.entity.User;
//import com.petservice.main.reservation.database.entity.ReservationEntity;
import lombok.Getter;
import lombok.Setter;

@Table(name = "review")
@Entity
@Getter
@Setter
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne
//    @JoinColumn(name = "reservation_id")
//    private ReservationEntity reservation;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "report_count")
    private Integer reportCount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "pet_business_name", length = 200)
    private String petBusinessName;

    @Column(name = "pet_business_location", length = 200)
    private String petBusinessLocation;
}