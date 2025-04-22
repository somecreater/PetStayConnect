package com.petservice.main.qna.database.repository;

import com.petservice.main.qna.database.entity.QnaAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QnaAnswerRepository extends JpaRepository<QnaAnswer,Long> {

}
