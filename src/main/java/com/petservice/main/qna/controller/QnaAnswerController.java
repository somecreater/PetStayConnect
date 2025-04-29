package com.petservice.main.qna.controller;

import com.petservice.main.qna.database.dto.QnaAnswerDTO;
import com.petservice.main.qna.service.answer.QnaAnswerServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/qnas/{question_id}/answer")
@RequiredArgsConstructor
public class QnaAnswerController {
    private final QnaAnswerServiceInterface service;

    @PostMapping
    public ResponseEntity<?> createAnswer(
            @PathVariable("question_id") Long questionId,
            @RequestBody QnaAnswerDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("내용은 필수입니다.");
        }
        QnaAnswerDTO created = service.createAnswer(
                questionId, dto, principal.getUsername()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<?> listAnswers(
            @PathVariable("question_id") Long questionId
    ) {
        List<QnaAnswerDTO> answers = service.getAnswersByPost(questionId);
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/{answer_id}")
    public ResponseEntity<?> getAnswer(
        @PathVariable("question_id") Long questionId,
        @PathVariable("answer_id") Long answerId){

        Map<String,Object> result = new HashMap<>();
        QnaAnswerDTO qnaAnswerDTO = service.getAnswerById(answerId);

        if(qnaAnswerDTO!=null){
            if(Objects.equals(qnaAnswerDTO.getPostId(), questionId)){
                result.put("result", true);
                result.put("message", "답변을 가져옵니다.");
                result.put("answer",qnaAnswerDTO);
            }else{
                result.put("result", false);
                result.put("message", "답변이 해당 질문에 존재하지 않습니다.");
            }

        }else{
            result.put("result", false);
            result.put("message", "답변이 존재하지 않습니다.");
        }
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{answer_id}")
    public ResponseEntity<?> updateAnswer(
            @PathVariable("question_id") Long questionId,
            @PathVariable("answer_id") Long answerId,
            @RequestBody QnaAnswerDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("내용은 필수입니다.");
        }
        try {
            QnaAnswerDTO updated = service.updateAnswer(
                    questionId, answerId, dto, principal.getUsername()
            );
            return ResponseEntity.ok(updated);
        } catch (AccessDeniedException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{answer_id}")
    public ResponseEntity<?> deleteAnswer(
            @PathVariable("question_id") Long questionId,
            @PathVariable("answer_id") Long answerId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }
        try {
            service.deleteAnswer(
                    questionId, answerId, principal.getUsername()
            );
            return ResponseEntity.noContent().build();
        } catch (AccessDeniedException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @PostMapping("/{answer_id}/accept")
    public ResponseEntity<?> adoptAnswer(
            @PathVariable("question_id") Long questionId,
            @PathVariable("answer_id") Long answerId,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        if (principal == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }
        try {
            QnaAnswerDTO adopted = service.adoptAnswer(
                    questionId, answerId, principal.getUsername()
            );
            return ResponseEntity.ok(adopted);
        } catch (IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        } catch (AccessDeniedException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(e.getMessage());
        } catch (EntityNotFoundException | IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
