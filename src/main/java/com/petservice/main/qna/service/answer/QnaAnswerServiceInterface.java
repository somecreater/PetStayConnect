package com.petservice.main.qna.service.answer;

import com.petservice.main.qna.database.dto.QnaAnswerDTO;

import java.util.List;

public interface QnaAnswerServiceInterface {
    QnaAnswerDTO createAnswer(Long postId, QnaAnswerDTO dto, String userLoginId);

    List<QnaAnswerDTO> getAnswersByPost(Long postId);

    QnaAnswerDTO updateAnswer(Long postId, Long answerId, QnaAnswerDTO dto, String userLoginId);

    void deleteAnswer(Long postId, Long answerId, String userLoginId);

    QnaAnswerDTO adoptAnswer(Long postId, Long answerId, String userLoginId);

}
