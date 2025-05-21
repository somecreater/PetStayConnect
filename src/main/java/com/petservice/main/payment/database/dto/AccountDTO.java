package com.petservice.main.payment.database.dto;


import com.petservice.main.payment.database.entity.AccountType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
  private Long id;
  private AccountType accountType;
  private BigDecimal amount;
  private Long userId;
  private String userLoginId;
}
