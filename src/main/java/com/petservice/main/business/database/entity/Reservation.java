package com.petservice.main.business.database.entity;

import com.petservice.main.common.database.entity.TimeEntity;
import com.petservice.main.payment.database.entity.Payment;
import com.petservice.main.user.database.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "reservation")
@Getter
@Setter
public class Reservation extends TimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_business_id", nullable = false)
    private PetBusiness petBusiness;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_business_room_id", nullable = true)
    private PetBusinessRoom petBusinessRoom;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    @Column(name = "special_requests", columnDefinition = "TEXT")
    private String specialRequests;

    @Column(name = "business_reservation_info", columnDefinition = "TEXT")
    private String businessReservationInfo;

    @Column(nullable = false)
    private Integer period;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<PetReservation> petReservations = new ArrayList<>();

}
