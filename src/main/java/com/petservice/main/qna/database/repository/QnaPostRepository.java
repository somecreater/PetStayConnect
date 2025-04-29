package com.petservice.main.qna.database.repository;

import com.petservice.main.qna.database.entity.QnaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {

    Page<QnaPost> findAllByOrderByCreatedAtDesc(Pageable pageable);

    List<QnaPost> findByUser_UserLoginId(String userLoginId);


}
