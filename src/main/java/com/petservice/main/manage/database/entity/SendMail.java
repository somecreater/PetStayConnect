package com.petservice.main.manage.database.entity;

import lombok.Data;

@Data
public class SendMail {
  private String userLoginId;
  private String title;
  private String content;
}
