package com.petservice.main.qna.database.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QnaAnswerDTO {
  private Long id;
  private Long postId;
  private String userLoginId;
  private Long userId;
  private String content;
  private Integer score;
  private Boolean isAdopted;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
