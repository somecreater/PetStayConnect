package com.petservice.main.payment.database.dto;

import com.petservice.main.payment.database.entity.PaymentStatus;
import com.petservice.main.payment.database.entity.RefundStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
/*
ID, 예약 ID, Iamport 고유 거래 ID, 주문 번호, 금액,
수수료율, 서비스 수수료, 결제 상태, 결제 방법,
거래 ID, 거래 시간, 환불 상태, 환불 금액
*/
@Data
public class PaymentDTO {

    private Long id;
    private Long reservationId;
    private String impUid;
    private String merchantUid;
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
