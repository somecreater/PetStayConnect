package com.petservice.main.qna.database.repository;

import com.petservice.main.qna.database.entity.QnaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QnaAnswerRepository extends JpaRepository<QnaAnswer, Long> {
    List<QnaAnswer> findByPostId(Long postId);

    boolean existsByPostIdAndIsAdoptedTrue(Long postId);

    long deleteByPost_Id(Long id);
}
