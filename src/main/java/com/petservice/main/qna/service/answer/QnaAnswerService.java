package com.petservice.main.qna.service.answer;

import com.petservice.main.qna.database.dto.QnaAnswerDTO;
import com.petservice.main.qna.database.entity.QnaAnswer;
import com.petservice.main.qna.database.entity.QnaPost;
import com.petservice.main.qna.database.mapper.QnaAnswerMapper;
import com.petservice.main.qna.database.repository.QnaAnswerRepository;
import com.petservice.main.qna.database.repository.QnaPostRepository;
import com.petservice.main.user.database.entity.User;
import com.petservice.main.user.database.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class QnaAnswerService implements QnaAnswerServiceInterface {
    private final QnaAnswerRepository answerRepo;
    private final QnaPostRepository postRepo;
    private final UserRepository userRepo;
    private final QnaAnswerMapper mapper;

    @Override
    public QnaAnswerDTO createAnswer(Long postId, QnaAnswerDTO dto, String userLoginId) {
        QnaPost post = findPost(postId);
        User businessOwner = findBusinessOwner(userLoginId);

        dto.setPostId(post.getId());
        dto.setUserId(businessOwner.getId());
        QnaAnswer saved = answerRepo.save(mapper.toEntity(dto));
        return mapper.toDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<QnaAnswerDTO> getAnswersByPost(Long postId) {
        return answerRepo.findByPostId(postId)
                .stream()
                .map(mapper::toBasicDTO)
                .collect(Collectors.toList());
    }

    @Override
    public QnaAnswerDTO updateAnswer(Long postId, Long answerId, QnaAnswerDTO dto, String userLoginId) {
        QnaAnswer answer = findAnswerForPost(postId, answerId);
        verifyOwner(answer, userLoginId);

        answer.setContent(dto.getContent());
        answer.setScore(dto.getScore());
        // 더티 체킹으로 엔티티 자동 업데이트
        return mapper.toDTO(answer);
    }

    @Override
    public void deleteAnswer(Long postId, Long answerId, String userLoginId) {
        QnaAnswer answer = findAnswerForPost(postId, answerId);
        verifyOwner(answer, userLoginId);

        answerRepo.delete(answer);
    }

    @Override
    public QnaAnswerDTO adoptAnswer(Long postId, Long answerId, String userLoginId) {
        QnaAnswer answer = findAnswerForPost(postId, answerId);
        verifyPostOwner(answer.getPost(), userLoginId);

        // 중복 채택 방지
        if (Boolean.TRUE.equals(answer.getIsAdopted())) {
            throw new IllegalStateException("Answer already adopted");
        }
        // 이전 채택 상태 초기화
        answerRepo.findByPostId(postId)
                .stream()
                .filter(QnaAnswer::getIsAdopted)
                .forEach(prev -> prev.setIsAdopted(false));

        // 해당 답변 채택 처리
        answer.setIsAdopted(true);
        // 더티 체킹으로 변경 내용 자동 반영

        // 사업자 보상 점수 증가
        User businessOwner = answer.getUser();
        businessOwner.setQnaScore((businessOwner.getQnaScore() == null ? 0 : businessOwner.getQnaScore()) + 1);
        // businessOwner 영속성 컨텍스트 관리로 점수 변경 자동 저장

        return mapper.toDTO(answer);
    }

    //----- 헬퍼 메서드 -----//

    private QnaPost findPost(Long postId) {
        return postRepo.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Question not found"));
    }

    private User findBusinessOwner(String loginId) {
        User user = userRepo.findByUserLoginIdWithBusiness(loginId)
                .orElseThrow(() -> new EntityNotFoundException("Business owner not found"));
        if (user.getPetBusiness() == null) {
            throw new AccessDeniedException("Only business owners can post answers");
        }
        return user;
    }

    private QnaAnswer findAnswerForPost(Long postId, Long answerId) {
        QnaAnswer answer = answerRepo.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer not found"));
        if (!answer.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("Answer does not belong to the specified post");
        }
        return answer;
    }

    private void verifyOwner(QnaAnswer answer, String loginId) {
        if (!answer.getUser().getUserLoginId().equals(loginId)) {
            throw new AccessDeniedException("Not your answer");
        }
    }

    private void verifyPostOwner(QnaPost post, String loginId) {
        if (!post.getUser().getUserLoginId().equals(loginId)) {
            throw new AccessDeniedException("Only post owner can adopt an answer");
        }
    }
}
