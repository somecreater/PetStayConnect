package com.petservice.main.bookmark.service;

import com.petservice.main.bookmark.database.dto.BookmarkDTO;
import com.petservice.main.bookmark.database.entity.Bookmark;
import com.petservice.main.bookmark.database.entity.Bookmark.ItemType;
import com.petservice.main.bookmark.database.mapper.BookmarkMapper;
import com.petservice.main.bookmark.database.repository.BookmarkRepository;
import com.petservice.main.qna.database.repository.QnaPostRepository;
// import com.petservice.main.hotel.database.repository.HotelRepository; // 호텔 리포지토리 필요시 주입
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkServiceInterface {

    private final BookmarkRepository bookmarkRepository;
    private final BookmarkMapper bookmarkMapper;
    private final QnaPostRepository qnaPostRepository;
    // private final HotelRepository hotelRepository; // 호텔 리포지토리 필요시 주입

    @Override
    @Transactional
    public void addBookmark(String userId, ItemType itemType, Long itemId) {
        if (bookmarkRepository.existsByUserIdAndItemTypeAndItemId(userId, itemType, itemId)) return;
        boolean exists = false;
        if (itemType == ItemType.QNA) {
            exists = qnaPostRepository.existsById(itemId);
        } else if (itemType == ItemType.HOTEL) {
            // exists = hotelRepository.existsById(itemId);
            exists = true; // 임시
        }
        if (!exists) throw new EntityNotFoundException("북마크하려는 항목이 존재하지 않습니다");
        Bookmark bookmark = new Bookmark();
        bookmark.setUserId(userId);
        bookmark.setItemType(itemType);
        bookmark.setItemId(itemId);
        bookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional
    public void removeBookmark(String userId, ItemType itemType, Long itemId) {
        bookmarkRepository.deleteByUserIdAndItemTypeAndItemId(userId, itemType, itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookmarkDTO> getBookmarksByUser(String userId) {
        return bookmarkRepository.findByUserId(userId).stream()
                .filter(bookmark -> isItemExists(bookmark.getItemType(), bookmark.getItemId()))
                .map(bookmarkMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void cleanupDeletedItem(ItemType itemType, Long itemId) {
        bookmarkRepository.deleteByItemTypeAndItemId(itemType, itemId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBookmarked(String userId, ItemType itemType, Long itemId) {
        return bookmarkRepository.existsByUserIdAndItemTypeAndItemId(userId, itemType, itemId);
    }

    private boolean isItemExists(ItemType itemType, Long itemId) {
        if (itemType == ItemType.QNA) {
            return qnaPostRepository.existsById(itemId);
        } else if (itemType == ItemType.HOTEL) {
            // return hotelRepository.existsById(itemId);
            return true; // 임시
        }
        return false;
    }
}
