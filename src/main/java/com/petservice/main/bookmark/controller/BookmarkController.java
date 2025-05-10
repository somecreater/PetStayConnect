package com.petservice.main.bookmark.controller;

import com.petservice.main.user.database.dto.BookmarkDTO;
import com.petservice.main.user.database.entity.BookmarkType;
import com.petservice.main.bookmark.service.BookmarkServiceInterface;
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
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkServiceInterface bookmarkService;

    @PostMapping
    public ResponseEntity<?> addBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam BookmarkType bookmarkType,
            @RequestParam Long targetId
    ) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null) {
            result.put("result", false);
            result.put("message", "로그인이 필요합니다");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        try {
            bookmarkService.addBookmark(principal.getUsername(), bookmarkType, targetId);
            result.put("result", true);
            result.put("message", "북마크가 추가되었습니다");
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            result.put("result", false);
            result.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(result);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> removeBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam BookmarkType bookmarkType,
            @RequestParam Long targetId
    ) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null) {
            result.put("result", false);
            result.put("message", "로그인이 필요합니다");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        bookmarkService.removeBookmark(principal.getUsername(), bookmarkType, targetId);
        result.put("result", true);
        result.put("message", "북마크가 삭제되었습니다");
        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> getBookmarks(
            @AuthenticationPrincipal CustomUserDetails principal
    ) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null) {
            result.put("result", false);
            result.put("message", "로그인이 필요합니다");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        List<BookmarkDTO> bookmarks = bookmarkService.getBookmarksByUser(principal.getUsername());
        result.put("result", true);
        result.put("bookmarks", bookmarks);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam BookmarkType bookmarkType,
            @RequestParam Long targetId
    ) {
        Map<String, Object> result = new HashMap<>();
        if (principal == null) {
            result.put("result", false);
            result.put("message", "로그인이 필요합니다");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(result);
        }
        boolean isBookmarked = bookmarkService.isBookmarked(principal.getUsername(), bookmarkType, targetId);
        result.put("result", true);
        result.put("isBookmarked", isBookmarked);
        return ResponseEntity.ok(result);
    }
}
