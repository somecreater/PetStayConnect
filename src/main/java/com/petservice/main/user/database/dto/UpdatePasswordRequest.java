package com.petservice.main.user.database.dto;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
  private String userLoginId;
  private String oldPassword;
  private String newPassword;
}
