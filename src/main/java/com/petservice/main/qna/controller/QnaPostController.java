package com.petservice.main.qna.controller;

import com.petservice.main.qna.database.dto.QnaPostDTO;
import com.petservice.main.qna.service.post.QnaPostServiceInterface;
import com.petservice.main.user.database.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/qnas")
@RequiredArgsConstructor
public class QnaPostController {

    private final QnaPostServiceInterface qnaPostService;

    //질문 등록
    @PostMapping("/question")
    public ResponseEntity<?> createPost(
            @RequestBody QnaPostDTO dto,
            @AuthenticationPrincipal CustomUserDetails principal
    ) {

        if (principal == null || principal.getUsername() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
        }

        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("제목은 필수입니다.");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("내용은 필수입니다.");
        }
        QnaPostDTO created = qnaPostService.createPost(
                dto,
                principal.getUsername()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }


    //전체 질문 목록 조회
    @GetMapping("/list")
    public ResponseEntity<Page<QnaPostDTO>> getPostsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<QnaPostDTO> posts = qnaPostService.getPostsPage(page, size);
        return ResponseEntity.ok(posts);
    }

    //내 질문 목록 조회
    @GetMapping("/mine")
    public ResponseEntity<List<QnaPostDTO>> getMyPosts(@AuthenticationPrincipal CustomUserDetails principal) {
        List<QnaPostDTO> posts = qnaPostService.getPostsByUserLoginId(principal.getUsername());
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{question_id}")
    public ResponseEntity<QnaPostDTO> getPostById(@PathVariable("question_id") Long postId) {
        try {
            QnaPostDTO post = qnaPostService.getPostById(postId);
            return ResponseEntity.ok(post);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{question_id}")
    public ResponseEntity<?> updatePost(@PathVariable("question_id") Long postId,
                                        @RequestBody QnaPostDTO dto,
                                        @AuthenticationPrincipal CustomUserDetails principal) {
        if (principal == null || principal.getUsername() == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("로그인이 필요합니다.");
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

