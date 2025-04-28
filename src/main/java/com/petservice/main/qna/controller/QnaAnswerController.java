package com.petservice.main.qna.controller;

import com.petservice.main.qna.database.dto.QnaAnswerDTO;
import com.petservice.main.qna.service.answer.QnaAnswerServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/qnas")
@RequiredArgsConstructor
public class QnaAnswerController {
    private final QnaAnswerServiceInterface service;

    //답변 등록 (사업자)
    @PostMapping("/question/{questionId}/answer")
    public ResponseEntity<QnaAnswerDTO> create(
            @PathVariable Long questionId,
            @RequestBody QnaAnswerDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        QnaAnswerDTO created = service.createAnswer(
                questionId, dto, userDetails.getUsername());
        return new ResponseEntity(created, HttpStatus.CREATED);
    }

    //특정 질문 답변 목록 조회
    @GetMapping("/answer/{questionId}/list")
    public ResponseEntity<List<QnaAnswerDTO>> list(
            @PathVariable Long questionID) {
        List<QnaAnswerDTO> answers = service.getAnswersByPost(questionID);
        return ResponseEntity.ok(answers);
    }

    //답변 수정 (사업자)
    @PutMapping("/answer/{answerId}")
    public ResponseEntity<QnaAnswerDTO> update(
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @RequestBody QnaAnswerDTO dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        QnaAnswerDTO updated = service.updateAnswer(
                questionId, answerId, dto, userDetails.getUsername());
        return ResponseEntity.ok(updated);
    }

    //답변 삭제
    @DeleteMapping("/answer/{answerId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        service.deleteAnswer(
                questionId, answerId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    //답변 채택 및 점수 부여
    @PostMapping("/answer/{answerId}/accept")
    public ResponseEntity<QnaAnswerDTO> adopt(
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        QnaAnswerDTO adopted = service.adoptAnswer(
                questionId, answerId, userDetails.getUsername());
        return ResponseEntity.ok(adopted);
    }
}
