package com.petservice.main.user.database.dto;

import com.petservice.main.user.database.entity.BookmarkType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookmarkDTO {

  private Long id;
  private BookmarkType bookmarkType;
  private Long targetId;
  private Long userId;
  private LocalDateTime createAt;
  private LocalDateTime updateAt;
}
