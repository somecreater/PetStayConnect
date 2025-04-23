package com.petservice.main.payment.database.mapper;

import com.petservice.main.payment.database.dto.PaymentDTO;
import com.petservice.main.payment.database.entity.Payment;
import com.petservice.main.payment.database.entity.PaymentStatus;
import com.petservice.main.payment.database.entity.RefundStatus;
import com.petservice.main.business.database.entity.Reservation;
import com.petservice.main.business.database.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final ReservationRepository reservationRepository;

    public Payment toEntity(PaymentDTO dto) {
        Payment entity = new Payment();

        if (dto.getId() != null) {
            entity.setId(dto.getId().longValue());
        }

        if (dto.getReservationId() != null) {
            Reservation reservation = reservationRepository.findById(dto.getReservationId().longValue())
                    .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
            entity.setReservation(reservation);
        }

        entity.setAmount(dto.getAmount());
        entity.setFeeRate(dto.getFeeRate());
        entity.setServiceFee(dto.getServiceFee());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setTransactionId(dto.getTransactionId());
        entity.setTransactionTime(dto.getTransactionTime());
        entity.setRefundAmount(dto.getRefundAmount());

        return entity;

    }
}