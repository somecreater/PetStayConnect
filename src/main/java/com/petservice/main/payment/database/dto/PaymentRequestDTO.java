package com.petservice.main.payment.database.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {
  private String impUid;
  private String merchantUid;
  private Long reservationId;
  private BigDecimal amount;
  private String payMethod;
  private String status;
  private Long paidAt;
}
