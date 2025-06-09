package com.petservice.main.manage.database.dto;

import com.petservice.main.manage.database.entity.Priority;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AnnouncementDTO {

  private Long id;
  private String title;
  private String type;
  private String content;
  private Priority priority;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
  private Long userId;
  private String userLoginId;
}
