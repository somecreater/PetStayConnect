package com.petservice.main.bookmark.service;

import com.petservice.main.bookmark.database.dto.BookmarkDTO;
import com.petservice.main.bookmark.database.entity.Bookmark.ItemType;
import java.util.List;

public interface BookmarkServiceInterface {
    void addBookmark(String userId, ItemType itemType, Long itemId);
    void removeBookmark(String userId, ItemType itemType, Long itemId);
    List<BookmarkDTO> getBookmarksByUser(String userId);
    void cleanupDeletedItem(ItemType itemType, Long itemId);
    boolean isBookmarked(String userId, ItemType itemType, Long itemId);
}
