package com.petservice.main.user.database.repository;

import com.petservice.main.user.database.entity.Bookmark;
import com.petservice.main.user.database.entity.BookmarkType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

    // 북마크 존재 여부 확인 (userLoginId, type, targetId)
    boolean existsByUser_UserLoginIdAndBookmarkTypeAndTargetId(String userLoginId, BookmarkType bookmarkType, Long targetId);

    // 북마크 삭제 (userLoginId, type, targetId)
    void deleteByUser_UserLoginIdAndBookmarkTypeAndTargetId(String userLoginId, BookmarkType bookmarkType, Long targetId);

    // 사용자별 북마크 목록 조회 (userLoginId)
    List<Bookmark> findByUser_UserLoginId(String userLoginId);

    // 특정 타입/타겟ID로 북마크 일괄 삭제 (예: QnA 게시글 삭제 시)
    void deleteByBookmarkTypeAndTargetId(BookmarkType bookmarkType, Long targetId);

    long deleteByUser_UserLoginId(String userLoginId);
}
