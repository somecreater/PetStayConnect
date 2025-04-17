package com.petservice.main.user.database.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@Data
@RequiredArgsConstructor
public class RefreshTokenDTO {
  private Long id;
  private String token;
  private Instant expiryDate;
  private String userLoginId;
  private Long userId;
}
