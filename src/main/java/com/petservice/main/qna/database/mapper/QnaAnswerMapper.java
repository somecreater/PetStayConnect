package com.petservice.main.qna.database.mapper;

import com.petservice.main.qna.database.dto.QnaAnswerDTO;
import com.petservice.main.qna.database.entity.QnaAnswer;
import com.petservice.main.qna.database.repository.QnaPostRepository;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QnaAnswerMapper {
  private final UserRepository userRepository;
  private final QnaPostRepository qnaPostRepository;

  public QnaAnswer toEntity(QnaAnswerDTO qnaAnswerDTO){

    QnaAnswer qnaAnswer=new QnaAnswer();
    qnaAnswer.setId(qnaAnswerDTO.getId());
    qnaAnswer.setContent(qnaAnswerDTO.getContent());
    qnaAnswer.setCreatedAt(qnaAnswerDTO.getCreatedAt());
    qnaAnswer.setUpdatedAt(qnaAnswerDTO.getUpdatedAt());
    if(qnaAnswerDTO.getUserId()!=null) {
      qnaAnswer.setUser(userRepository.findById(qnaAnswerDTO.getUserId()).orElse(null));
    }
    if(qnaAnswerDTO.getPostId()!=null) {
      qnaAnswer.setPost(qnaPostRepository.findById(qnaAnswerDTO.getPostId()).orElse(null));
    }
    return qnaAnswer;
  }

  public QnaAnswerDTO toDTO(QnaAnswer qnaAnswer){

    QnaAnswerDTO qnaAnswerDTO=new QnaAnswerDTO();
    qnaAnswerDTO.setId(qnaAnswer.getId());
    qnaAnswerDTO.setContent(qnaAnswer.getContent());
    qnaAnswerDTO.setCreatedAt(qnaAnswer.getCreatedAt());
    qnaAnswerDTO.setUpdatedAt(qnaAnswer.getUpdatedAt());
    if(qnaAnswer.getUser()!=null){
      qnaAnswerDTO.setUserId(qnaAnswer.getUser().getId());
    }
    if(qnaAnswer.getPost()!=null){
      qnaAnswerDTO.setPostId(qnaAnswer.getPost().getId());
    }
    return qnaAnswerDTO;
  }
}
