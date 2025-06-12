package com.petservice.main.qna.service.post;

import com.petservice.main.qna.database.dto.QnaPostDTO;
import com.petservice.main.qna.database.entity.QnaPost;
import com.petservice.main.qna.database.mapper.QnaPostMapper;
import com.petservice.main.qna.database.repository.QnaPostRepository;
import com.petservice.main.user.database.dto.CustomUserDetails;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QnaPostService implements QnaPostServiceInterface {

    private final QnaPostRepository qnaPostRepository;
    private final UserRepository userRepository;
    private final QnaPostMapper qnaPostMapper;

    @Override
    @Transactional
    public QnaPostDTO createPost(QnaPostDTO dto, CustomUserDetails principal) {
        User user = userRepository.findByUserLoginId(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        QnaPost post = qnaPostMapper.toEntity(dto);
        post.setUser(user);
        post.setCreatedAt(LocalDateTime.now());
        post.setUpdatedAt(LocalDateTime.now());
        QnaPost saved = qnaPostRepository.save(post);
        return qnaPostMapper.toDTO(saved);

    }

    //전체 질문 목록 조회
    @Override
    @Transactional(readOnly = true)
    public Page<QnaPostDTO> getPostsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return qnaPostRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .map(qnaPostMapper::toDTO);
    }


    //내가 쓴 질문 목록 조회
    @Override
    @Transactional(readOnly = true)

    public List<QnaPostDTO> getPostsByUserLoginId(String userLoginId) {
        return qnaPostRepository.findByUser_UserLoginId(userLoginId).stream()
                .map(qnaPostMapper::toBasicDTO)
                .collect(Collectors.toList());
    }


    //질문 상세 조회
    @Override
    @Transactional(readOnly = true)
    public QnaPostDTO getPostById(Long postId) {
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        return qnaPostMapper.toDTO(post);
    }

    @Override
    @Transactional
    public QnaPostDTO viewAndGet(Long postId) {
        qnaPostRepository.incrementViewCount(postId);
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        return qnaPostMapper.toDTO(post);
    }



    //질문 수정(작성자만 가능)
    @Override
    @Transactional
    public QnaPostDTO updatePost(Long postId, QnaPostDTO dto, String userLoginId) {
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        if (post.getUser() == null || !post.getUser().getUserLoginId().equals(userLoginId)) {
            throw new SecurityException("본인이 작성한 질문만 수정할 수 있습니다.");
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setCategory(dto.getCategory());
        post.setUpdatedAt(LocalDateTime.now());

        QnaPost saved = qnaPostRepository.save(post);
        return qnaPostMapper.toDTO(saved);
    }

    //질문 삭제(작성자만 가능)
    @Override
    @Transactional
    public void deletePost(Long postId, String userLoginId) {
        QnaPost post = qnaPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("질문을 찾을 수 없습니다."));

        if (post.getUser() == null || !post.getUser().getUserLoginId().equals(userLoginId)) {
            throw new SecurityException("본인이 작성한 질문만 삭제할 수 있습니다.");
        }

        qnaPostRepository.delete(post);
    }
}


