package com.petservice.main.qna.database.repository;

import com.petservice.main.qna.database.entity.QnaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {

    Page<QnaPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<QnaPost> findByUser_UserLoginId(String userLoginId);

    @Modifying
    @Query("UPDATE QnaPost p SET p.viewCount = p.viewCount + 1 WHERE p.id = :postId")
    int incrementViewCount(@Param("postId") Long postId);


}
