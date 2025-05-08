package com.petservice.main.bookmark.service;

import com.petservice.main.user.database.dto.BookmarkDTO;
import com.petservice.main.user.database.entity.Bookmark;
import com.petservice.main.user.database.entity.BookmarkType;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.mapper.BookmarkMapper;
import com.petservice.main.user.database.repository.BookmarkRepository;
import com.petservice.main.user.database.repository.UserRepository;
import com.petservice.main.qna.database.repository.QnaPostRepository;
import com.petservice.main.hotel.database.repository.HotelRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkServiceInterface {

    private final BookmarkRepository bookmarkRepository;
    private final UserRepository userRepository;
    private final QnaPostRepository qnaPostRepository;
    private final HotelRepository hotelRepository;
    private final BookmarkMapper bookmarkMapper;

    @Override
    @Transactional
    public BookmarkDTO createBookmark(Long userId, BookmarkType bookmarkType, Long targetId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        validateTargetExists(bookmarkType, targetId);

        Bookmark bookmark = new Bookmark();
        bookmark.setBookmarkType(bookmarkType);
        bookmark.setTargetId(targetId);
        bookmark.setUser(user);

        Bookmark saved = bookmarkRepository.save(bookmark);
        return bookmarkMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteBookmark(Long userId, BookmarkType bookmarkType, Long targetId) {
        bookmarkRepository.deleteByUser_IdAndBookmarkTypeAndTargetId(userId, bookmarkType, targetId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookmarkDTO> getBookmarksByUser(Long userId) {
        return bookmarkRepository.findByUser_Id(userId).stream()
                .map(bookmarkMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBookmarked(Long userId, BookmarkType bookmarkType, Long targetId) {
        return bookmarkRepository.existsByUser_IdAndBookmarkTypeAndTargetId(userId, bookmarkType, targetId);
    }

    private void validateTargetExists(BookmarkType type, Long targetId) {
        boolean exists = switch (type) {
            case QNA -> qnaPostRepository.existsById(targetId);
            case HOTEL -> hotelRepository.existsById(targetId);
        };
        if (!exists) throw new EntityNotFoundException("Target resource not found");
    }
}
