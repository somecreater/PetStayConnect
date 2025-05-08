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
    public ResponseEntity<?> createBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam BookmarkType bookmarkType,
            @RequestParam Long targetId) {

        Map<String, Object> response = new HashMap<>();
        try {
            BookmarkDTO created = bookmarkService.createBookmark(
                    principal.getUserId(),
                    bookmarkType,
                    targetId
            );
            response.put("result", true);
            response.put("data", created);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            response.put("result", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam BookmarkType bookmarkType,
            @RequestParam Long targetId) {

        Map<String, Object> response = new HashMap<>();
        try {
            bookmarkService.deleteBookmark(
                    principal.getUserId(),
                    bookmarkType,
                    targetId
            );
            response.put("result", true);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping
    public ResponseEntity<?> getBookmarks(
            @AuthenticationPrincipal CustomUserDetails principal) {

        Map<String, Object> response = new HashMap<>();
        try {
            List<BookmarkDTO> bookmarks = bookmarkService.getBookmarksByUser(
                    principal.getUserId()
            );
            response.put("result", true);
            response.put("data", bookmarks);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkBookmark(
            @AuthenticationPrincipal CustomUserDetails principal,
            @RequestParam BookmarkType bookmarkType,
            @RequestParam Long targetId) {

        Map<String, Object> response = new HashMap<>();
        try {
            boolean isBookmarked = bookmarkService.isBookmarked(
                    principal.getUserId(),
                    bookmarkType,
                    targetId
            );
            response.put("result", true);
            response.put("isBookmarked", isBookmarked);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("result", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
