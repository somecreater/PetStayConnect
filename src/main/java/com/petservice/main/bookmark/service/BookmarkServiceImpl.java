package com.petservice.main.bookmark.service;

import com.petservice.main.user.database.dto.BookmarkDTO;
import com.petservice.main.user.database.entity.Bookmark;
import com.petservice.main.user.database.entity.BookmarkType;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.mapper.BookmarkMapper;
import com.petservice.main.user.database.repository.BookmarkRepository;
import com.petservice.main.user.database.repository.UserRepository;
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
    private final BookmarkMapper bookmarkMapper;

    @Override
    @Transactional
    public void addBookmark(String userLoginId, BookmarkType bookmarkType, Long targetId) {
        // 이미 북마크했는지 확인 (커스텀 쿼리로 처리)
        boolean exists = bookmarkRepository.existsByUser_UserLoginIdAndBookmarkTypeAndTargetId(userLoginId, bookmarkType, targetId);
        if (exists) return;

        // 사용자 조회 (북마크 생성에 user 엔티티 필요)
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setBookmarkType(bookmarkType);
        bookmark.setTargetId(targetId);
        bookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional
    public void removeBookmark(String userLoginId, BookmarkType bookmarkType, Long targetId) {
        // 검증 없이 바로 삭제 (user 조회 없이)
        bookmarkRepository.deleteByUser_UserLoginIdAndBookmarkTypeAndTargetId(userLoginId, bookmarkType, targetId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookmarkDTO> getBookmarksByUser(String userLoginId) {
        // 커스텀 쿼리로 북마크 목록 조회
        return bookmarkRepository.findByUser_UserLoginId(userLoginId).stream()
                .map(bookmarkMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBookmarked(String userLoginId, BookmarkType bookmarkType, Long targetId) {
        // 커스텀 쿼리로 북마크 존재 여부 확인
        return bookmarkRepository.existsByUser_UserLoginIdAndBookmarkTypeAndTargetId(userLoginId, bookmarkType, targetId);
    }

    @Override
    @Transactional
    public void cleanupDeletedItem(BookmarkType bookmarkType, Long targetId) {
        // 커스텀 쿼리로 북마크 일괄 삭제
        bookmarkRepository.deleteByBookmarkTypeAndTargetId(bookmarkType, targetId);
    }

    @Override
    @Transactional
    public void cleanUserBookmark(String userLoginId){
        // 커스텀 쿼리로 회원의 북마크 일괄 삭제
        bookmarkRepository.deleteByUser_UserLoginId(userLoginId);
    }
}
