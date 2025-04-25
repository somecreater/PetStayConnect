package com.petservice.main.qna.database.repository;

import com.petservice.main.qna.database.entity.QnaPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaPostRepository extends JpaRepository<QnaPost, Long> {

    List<QnaPost> findAllByOrderByCreatedAtDesc();

    List<QnaPost> findByUser_UserLoginId(String userLoginId);

}
