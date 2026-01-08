package com.project.byeoryback.domain.post.controller;

import com.project.byeoryback.domain.post.dto.PostRequest;
import com.project.byeoryback.domain.post.dto.PostResponse;
import com.project.byeoryback.domain.post.service.PostService;
import com.project.byeoryback.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // 5. 프론트엔드 주소 명시 권장 (Vite 기본 포트)
public class PostController {

    private final PostService postService;

    // 1. 목록 조회 (로그인한 유저의 글만)
    @GetMapping
    public ResponseEntity<List<PostResponse>> getMyPosts(
            @AuthenticationPrincipal com.project.byeoryback.global.security.CustomUserDetails userDetails) {
        List<Post> posts;
        if (userDetails != null) {
            posts = postService.getPostsByUserId(userDetails.getUser().getId());
        } else {
            // 비로그인 시 빈 리스트 혹은 에러 처리 (여기선 빈 리스트)
            posts = List.of();
        }

        List<PostResponse> response = posts.stream()
                .map(PostResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    // 2. 저장 (Create)
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @AuthenticationPrincipal com.project.byeoryback.global.security.CustomUserDetails userDetails,
            @RequestBody PostRequest request) {
        Post post = postService.createPost(userDetails.getUser(), request);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    // 3. 수정 (Update)
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @AuthenticationPrincipal com.project.byeoryback.global.security.CustomUserDetails userDetails,
            @PathVariable Long id, @RequestBody PostRequest request) {
        Post post = postService.updatePost(userDetails.getUser(), id, request);
        return ResponseEntity.ok(PostResponse.from(post));
    }

    // 4. 삭제 (Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    // 5. 월별 요약 조회 (Calendar용)
    @GetMapping("/summary")
    public ResponseEntity<List<com.project.byeoryback.domain.post.dto.PostSummaryResponse>> getMonthlyPostSummary(
            @AuthenticationPrincipal com.project.byeoryback.global.security.CustomUserDetails userDetails,
            @RequestParam(value = "year") int year,
            @RequestParam(value = "month") int month) {

        if (userDetails == null) {
            return ResponseEntity.ok(List.of());
        }

        return ResponseEntity.ok(postService.getPostsSummaryByYearMonth(userDetails.getUser().getId(), year, month));
    }
}