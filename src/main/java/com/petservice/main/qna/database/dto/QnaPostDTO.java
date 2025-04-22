package com.petservice.main.qna.database.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class QnaPostDTO {
  private Long id;
  private Long userId;
  private String title;
  private String content;
  private String category;
  private Integer viewCount;
  private List<QnaAnswerDTO> qnaAnswerDTOList = new ArrayList<>();
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
