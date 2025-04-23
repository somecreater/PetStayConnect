package com.petservice.main.review.database.entity;

import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.common.database.entity.TimeEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.petservice.main.user.database.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Table(name = "review")
@Entity
@Getter
@Setter
public class Review extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Reservation reservation;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private User user;

    @Column
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "report_count")
    private Integer reportCount;

    @Column(name = "pet_business_name", length = 200)
    private String petBusinessName;

    @Column(name = "pet_business_location", length = 200)
    private String petBusinessLocation;
}
