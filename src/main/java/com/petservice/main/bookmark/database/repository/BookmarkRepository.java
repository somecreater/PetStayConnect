package com.petservice.main.bookmark.database.repository;

import com.petservice.main.bookmark.database.entity.Bookmark;
import com.petservice.main.bookmark.database.entity.Bookmark.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    List<Bookmark> findByUserId(String userId);
    boolean existsByUserIdAndItemTypeAndItemId(String userId, ItemType itemType, Long itemId);
    void deleteByUserIdAndItemTypeAndItemId(String userId, ItemType itemType, Long itemId);
    void deleteByItemTypeAndItemId(ItemType itemType, Long itemId);
}
