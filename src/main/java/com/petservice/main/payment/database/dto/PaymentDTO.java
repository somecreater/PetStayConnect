package com.petservice.main.payment.database.dto;

import com.petservice.main.payment.database.entity.PaymentStatus;
import com.petservice.main.payment.database.entity.RefundStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentDTO {

    private Integer id;
    private Integer reservationId;
    private BigDecimal amount;
    private BigDecimal feeRate;
    private BigDecimal serviceFee;
    private PaymentStatus paymentState;
    private String paymentMethod;
    private String transactionId;
    private LocalDateTime transactionTime;
    private RefundStatus refundState;
    private BigDecimal refundAmount;
}
