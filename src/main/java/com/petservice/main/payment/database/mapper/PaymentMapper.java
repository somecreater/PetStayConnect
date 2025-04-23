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
        entity.setId(dto.getId());
        entity.setAmount(dto.getAmount());
        entity.setFeeRate(dto.getFeeRate());
        entity.setServiceFee(dto.getServiceFee());
        entity.setPaymentStatus(dto.getPaymentStatus());
        entity.setPaymentMethod(dto.getPaymentMethod());
        entity.setTransactionId(dto.getTransactionId());
        entity.setTransactionTime(dto.getTransactionTime());
        entity.setRefundStatus(dto.getRefundStatus());
        entity.setRefundAmount(dto.getRefundAmount());
        if(dto.getReservationId() != null){
            entity.setReservation(reservationRepository.findById(dto.getReservationId()).orElse(null));
        }
        return entity;
    }

    public PaymentDTO toDTO(Payment payment){

        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setId(payment.getId());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setFeeRate(payment.getFeeRate());
        paymentDTO.setServiceFee(payment.getServiceFee());
        paymentDTO.setPaymentStatus(payment.getPaymentStatus());
        paymentDTO.setPaymentMethod(payment.getPaymentMethod());
        paymentDTO.setTransactionId(payment.getTransactionId());
        paymentDTO.setTransactionTime(payment.getTransactionTime());
        paymentDTO.setPaymentStatus(payment.getPaymentStatus());
        paymentDTO.setRefundAmount(payment.getRefundAmount());
        if(payment.getReservation() != null){
            paymentDTO.setReservationId(payment.getReservation().getId());
        }
        return paymentDTO;
    }

}
