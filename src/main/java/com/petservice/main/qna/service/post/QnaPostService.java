package com.petservice.main.qna.service.post;

import com.petservice.main.qna.database.dto.QnaPostDTO;
import com.petservice.main.qna.database.entity.QnaPost;
import com.petservice.main.qna.database.mapper.QnaPostMapper;
import com.petservice.main.qna.database.repository.QnaPostRepository;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional

public class QnaPostService implements QnaPostServiceInterface {

    private final QnaPostRepository qnaPostRepository;
    private final UserRepository userRepository;
    private final QnaPostMapper qnaPostMapper;

    @Override
    public void createPost(QnaPostDTO dto, String userLoginId) {
        User user = userRepository.findByUserLoginId(userLoginId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        QnaPost post = new QnaPost();
        post.setUser(user);
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(dto.getCategory());
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());

        qnaPostRepository.save(post);
    }

    //전체 질문 목록 조회
    @Override
    public List<QnaPostDTO> getAllPosts() {
        return qnaPostRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(qnaPostMapper::toBasicDTO)
                .collect(Collectors.toList());
    }


    //내가 쓴 질문 목록 조회
    @Override
    public List<QnaPostDTO> getPostsByUserLoginId(String userLoginId) {
        return qnaPostRepository.findByUser_UserLoginId(userLoginId).stream()
                .map(qnaPostMapper::toBasicDTO)
                .collect(Collectors.toList());
    }


    //질문 상세 조회
    @Override
    public QnaPostDTO getPostById(Long postId) {
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));
        return qnaPostMapper.toDTO(post);
    }

    //질문 수정(작성자만 가능)
    @Override
    public void updatePost(Long postId, QnaPostDTO dto, String userLoginId) {
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        if (post.getUser() == null || !post.getUser().getUserLoginId().equals(userLoginId)) {
            throw new SecurityException("본인이 작성한 질문만 수정할 수 있습니다.");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(dto.getCategory());
        post.setUpdatedAt(LocalDateTime.now());

        qnaPostRepository.save(post);
    }

    //질문 삭제(작성자만 가능)
    @Override
    public void deletePost(Long postId, String userLoginId) {
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        if (post.getUser() == null || !post.getUser().getUserLoginId().equals(userLoginId)) {
            throw new SecurityException(("본인이 작성한 질문만 삭제할 수 있습니다."));
        }

        qnaPostRepository.delete(post);
    }


}
