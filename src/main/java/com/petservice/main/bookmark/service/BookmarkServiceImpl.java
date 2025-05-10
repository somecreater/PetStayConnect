package com.petservice.main.bookmark.service;

import com.petservice.main.user.database.dto.BookmarkDTO;
import com.petservice.main.user.database.entity.Bookmark;
import com.petservice.main.user.database.entity.BookmarkType;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.mapper.BookmarkMapper;
import com.petservice.main.user.database.repository.BookmarkRepository;
import com.petservice.main.user.database.repository.UserRepository;
import com.petservice.main.qna.database.repository.QnaPostRepository;
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
    private final UserRepository userRepository;
    private final QnaPostRepository qnaPostRepository;
    private final BookmarkMapper bookmarkMapper;

    @Override
    @Transactional
    public void addBookmark(String userLoginId, BookmarkType bookmarkType, Long targetId) {
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        // QNA 게시글 존재 여부 확인
        if (bookmarkType == BookmarkType.QNA && !qnaPostRepository.existsById(targetId)) {
            throw new EntityNotFoundException("QnA 게시글이 존재하지 않습니다.");
        }

        // 이미 북마크했는지 확인 (메모리상에서 체크)
        boolean exists = bookmarkRepository.findAll().stream()
                .anyMatch(b -> b.getUser().getId().equals(user.getId())
                        && b.getBookmarkType() == bookmarkType
                        && b.getTargetId().equals(targetId));
        if (exists) return;

        Bookmark bookmark = new Bookmark();
        bookmark.setUser(user);
        bookmark.setBookmarkType(bookmarkType);
        bookmark.setTargetId(targetId);
        bookmarkRepository.save(bookmark);
    }

    @Override
    @Transactional
    public void removeBookmark(String userLoginId, BookmarkType bookmarkType, Long targetId) {
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        // 메모리상에서 찾아서 삭제
        bookmarkRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(user.getId())
                        && b.getBookmarkType() == bookmarkType
                        && b.getTargetId().equals(targetId))
                .forEach(bookmarkRepository::delete);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookmarkDTO> getBookmarksByUser(String userLoginId) {
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        return bookmarkRepository.findAll().stream()
                .filter(b -> b.getUser().getId().equals(user.getId()))
                .filter(b -> b.getBookmarkType() != BookmarkType.QNA || qnaPostRepository.existsById(b.getTargetId()))
                .map(bookmarkMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isBookmarked(String userLoginId, BookmarkType bookmarkType, Long targetId) {
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다"));

        return bookmarkRepository.findAll().stream()
                .anyMatch(b -> b.getUser().getId().equals(user.getId())
                        && b.getBookmarkType() == bookmarkType
                        && b.getTargetId().equals(targetId));
    }

    @Override
    @Transactional
    public void cleanupDeletedItem(BookmarkType bookmarkType, Long targetId) {
        List<Bookmark> toDelete = bookmarkRepository.findAll().stream()
                .filter(b -> b.getBookmarkType() == bookmarkType && b.getTargetId().equals(targetId))
                .collect(Collectors.toList());
        bookmarkRepository.deleteAll(toDelete);
    }
}
