package com.petservice.main.qna.database.mapper;

import com.petservice.main.qna.database.dto.QnaPostDTO;
import com.petservice.main.qna.database.entity.QnaPost;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QnaPostMapper {

  private final UserRepository userRepository;
  private final QnaAnswerMapper qnaAnswerMapper;

  public QnaPost toEntity(QnaPostDTO qnaPostDTO){

    QnaPost qnaPost=new QnaPost();
    qnaPost.setId(qnaPostDTO.getId());
    qnaPost.setCategory(qnaPostDTO.getCategory());
    qnaPost.setTitle(qnaPostDTO.getTitle());
    qnaPost.setContent(qnaPostDTO.getContent());
    qnaPost.setViewCount(qnaPostDTO.getViewCount());
    qnaPost.setCreatedAt(qnaPostDTO.getCreatedAt());
    qnaPost.setUpdatedAt(qnaPostDTO.getUpdatedAt());
    if(qnaPostDTO.getUserId()!=null){
      qnaPost.setUser(userRepository.findById(qnaPostDTO.getUserId()).orElse(null));
    }
    if(qnaPostDTO.getQnaAnswerDTOList()!=null){
      qnaPost.setQnaAnswerList(qnaPostDTO.getQnaAnswerDTOList().stream()
          .map(qnaAnswerMapper::toEntity).toList());
    }
    return qnaPost;
  }

  public QnaPostDTO toDTO(QnaPost qnaPost){
    QnaPostDTO qnaPostDTO=new QnaPostDTO();
    qnaPostDTO.setId(qnaPost.getId());
    qnaPostDTO.setCategory(qnaPost.getCategory());
    qnaPostDTO.setTitle(qnaPost.getTitle());
    qnaPostDTO.setContent(qnaPost.getContent());
    qnaPostDTO.setViewCount(qnaPost.getViewCount());
    qnaPostDTO.setCreatedAt(qnaPost.getCreatedAt());
    qnaPostDTO.setUpdatedAt(qnaPost.getUpdatedAt());
    if(qnaPost.getUser()!=null){
      qnaPostDTO.setUserId(qnaPost.getUser().getId());
    }
    if(qnaPost.getQnaAnswerList()!=null){
      qnaPostDTO.setQnaAnswerDTOList(qnaPost.getQnaAnswerList().stream()
          .map(qnaAnswerMapper::toDTO).toList());
    }
    return qnaPostDTO;
  }
}
