package com.petservice.main.bookmark.service;

import com.petservice.main.user.database.dto.BookmarkDTO;
import com.petservice.main.user.database.entity.BookmarkType;
import java.util.List;

public interface BookmarkServiceInterface {
    void addBookmark(String userLoginId, BookmarkType bookmarkType, Long targetId);
    void removeBookmark(String userLoginId, BookmarkType bookmarkType, Long targetId);
    List<BookmarkDTO> getBookmarksByUser(String userLoginId);
    boolean isBookmarked(String userLoginId, BookmarkType bookmarkType, Long targetId);
    void cleanupDeletedItem(BookmarkType bookmarkType, Long targetId);
}
