package com.petservice.main.bookmark.database.dto;

import com.petservice.main.bookmark.database.entity.Bookmark.ItemType;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookmarkDTO {
    private Long id;
    private String userId;
    private ItemType itemType;
    private Long itemId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
