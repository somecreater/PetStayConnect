package com.petservice.main.bookmark.database.mapper;

import com.petservice.main.bookmark.database.dto.BookmarkDTO;
import com.petservice.main.bookmark.database.entity.Bookmark;
import org.springframework.stereotype.Component;

@Component
public class BookmarkMapper {

    public BookmarkDTO toDTO(Bookmark bookmark) {
        BookmarkDTO dto = new BookmarkDTO();
        dto.setId(bookmark.getId());
        dto.setUserId(bookmark.getUserId());
        dto.setItemType(bookmark.getItemType());
        dto.setItemId(bookmark.getItemId());
        dto.setCreatedAt(bookmark.getCreatedAt());
        dto.setUpdatedAt(bookmark.getUpdatedAt());
        return dto;
    }

    public Bookmark toEntity(BookmarkDTO dto) {
        Bookmark bookmark = new Bookmark();
        bookmark.setId(dto.getId());
        bookmark.setUserId(dto.getUserId());
        bookmark.setItemType(dto.getItemType());
        bookmark.setItemId(dto.getItemId());
        return bookmark;
    }
}
