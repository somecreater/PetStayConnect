package com.petservice.main.bookmark.service;

import com.petservice.main.user.database.dto.BookmarkDTO;
import com.petservice.main.user.database.entity.BookmarkType;
import java.util.List;

public interface BookmarkServiceInterface {
    public void addBookmark(String userLoginId, BookmarkType bookmarkType, Long targetId);
    public void removeBookmark(String userLoginId, BookmarkType bookmarkType, Long targetId);
    public List<BookmarkDTO> getBookmarksByUser(String userLoginId);
    public boolean isBookmarked(String userLoginId, BookmarkType bookmarkType, Long targetId);
    public void cleanupDeletedItem(BookmarkType bookmarkType, Long targetId);
    public void cleanUserBookmark(String userLoginId);
}
