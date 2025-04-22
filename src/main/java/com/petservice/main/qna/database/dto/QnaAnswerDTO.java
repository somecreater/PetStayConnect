package com.petservice.main.qna.database.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QnaAnswerDTO {
  private Long id;
  private Long postId;
  private Long userId;
  private String content;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
