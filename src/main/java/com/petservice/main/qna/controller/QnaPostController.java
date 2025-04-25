package com.petservice.main.qna.controller;

import com.petservice.main.qna.database.dto.QnaPostDTO;
import com.petservice.main.qna.service.post.QnaPostServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/qnas")
@RequiredArgsConstructor
public class QnaPostController {

    private final QnaPostServiceInterface qnaPostService;

    //질문 등록
    @PostMapping("/question")
    public ResponseEntity<?> createPost(@RequestBody QnaPostDTO dto,
                                        @AuthenticationPrincipal CustomUserDetails principal,
                                        @RequestParam(name = "userLoginId") String userLoginId) {
        Map<String, Object> result = new HashMap<>();

        if (principal == null || !principal.getUsername().equals(userLoginId)) {
            result.put("auth", false);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }

        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("제목은 필수입니다.");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("내용은 필수입니다.");
        }
        qnaPostService.createPost(dto, userLoginId);
        return ResponseEntity.ok("질문 등록 완료");
    }

    //전체 질문 목록 조회
    @GetMapping("/list")
    public ResponseEntity<List<QnaPostDTO>> getAllPosts() {
        List<QnaPostDTO> posts = qnaPostService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    //내 질문 목록 조회
    @GetMapping("/mine")
    public ResponseEntity<?> getMyPosts(@AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null || principal.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        List<QnaPostDTO> posts = qnaPostService.getPostsByUserLoginId(principal.getUsername());
        return ResponseEntity.ok(posts);
    }

    //질문 상세 조회-> 아직 test 안 함
    @GetMapping("/{question_id}")
    public ResponseEntity<?> getPostById(@PathVariable("question_id") Long postId) {
        try {
            QnaPostDTO post = qnaPostService.getPostById(postId);
            return ResponseEntity.ok(post);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{question_id}")
    public ResponseEntity<?> updatePost(@PathVariable("question_id") Long postId,
                                        @RequestBody QnaPostDTO dto,
                                        @AuthenticationPrincipal CustomUserDetails principal) {

        if (principal == null || principal.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("제목은 필수입니다.");
        }

        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("내용은 필수입니다.");
        }

        try {
            qnaPostService.updatePost(postId, dto, principal.getUsername());
            return ResponseEntity.ok("질문 수정 완료");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{question_id}")
    public ResponseEntity<?> deletePost(@PathVariable("question_id") Long postId,
                                        @AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null || principal.getUsername() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }

        try {
            qnaPostService.deletePost(postId, principal.getUsername());
            return ResponseEntity.ok("질문 삭제 완료");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
}

