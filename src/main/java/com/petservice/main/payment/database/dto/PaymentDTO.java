package com.petservice.main.payment.database.dto;

import com.petservice.main.payment.database.entity.PaymentStatus;
import com.petservice.main.payment.database.entity.RefundStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {

    private Long id;
    private Long reservationId;
    private BigDecimal amount;
    private BigDecimal feeRate;
    private BigDecimal serviceFee;
    private PaymentStatus paymentStatus;
    private String paymentMethod;
    private String transactionId;
    private LocalDateTime transactionTime;
    private RefundStatus refundStatus;
    private BigDecimal refundAmount;
}
