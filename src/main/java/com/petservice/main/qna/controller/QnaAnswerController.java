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

import java.util.List;

@RestController
@RequestMapping("/api/qnas")
@RequiredArgsConstructor
public class QnaAnswerController {
    private final QnaAnswerServiceInterface service;

    @PostMapping("/question/{question_id}/answer")
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

    @GetMapping("/answer/{answer_id}")
    public ResponseEntity<?> listAnswers(
            @PathVariable("answer_id") Long questionId
    ) {
        List<QnaAnswerDTO> answers = service.getAnswersByPost(questionId);
        return ResponseEntity.ok(answers);
    }

    @PutMapping("/answer/{answer_id}")
    public ResponseEntity<?> updateAnswer(
            @PathVariable("answer_id") Long answerId,
            @RequestParam(name = "question_id") Long questionId,
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

    @DeleteMapping("/answer/{answer_id}")
    public ResponseEntity<?> deleteAnswer(
            @PathVariable("answer_id") Long answerId,
            @RequestParam(name = "question_id") Long questionId,
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

    @PostMapping("/answer/{answer_id}/accept")
    public ResponseEntity<?> adoptAnswer(
            @PathVariable("answer_id") Long answerId,
            @RequestParam(name = "question_id") Long questionId,
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
