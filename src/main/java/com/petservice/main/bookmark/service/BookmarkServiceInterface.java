package com.petservice.main.bookmark.service;

import com.petservice.main.user.database.dto.BookmarkDTO;
import com.petservice.main.user.database.entity.BookmarkType;
import java.util.List;

public interface BookmarkServiceInterface {
    BookmarkDTO createBookmark(Long userId, BookmarkType bookmarkType, Long targetId);
    void deleteBookmark(Long userId, BookmarkType bookmarkType, Long targetId);
    List<BookmarkDTO> getBookmarksByUser(Long userId);
    boolean isBookmarked(Long userId, BookmarkType bookmarkType, Long targetId);
}
