package com.petservice.main.payment.database.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentCancelRequestDTO {
  private String impUid;
  private String merchantUid;
  private BigDecimal amount;
  private String reason;
}
