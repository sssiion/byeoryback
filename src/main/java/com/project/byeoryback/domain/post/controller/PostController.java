package com.project.byeoryback.domain.post.controller;

import com.project.byeoryback.domain.post.dto.PostRequest;
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

    // 1. 목록 조회
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 2. 저장 (Create)
    @PostMapping
    public ResponseEntity<Post> createPost(
            @AuthenticationPrincipal com.project.byeoryback.global.security.CustomUserDetails userDetails,
            @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.createPost(userDetails.getUser(), request));
    }

    // 3. 수정 (Update)
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(
            @AuthenticationPrincipal com.project.byeoryback.global.security.CustomUserDetails userDetails,
            @PathVariable Long id, @RequestBody PostRequest request) {
        return ResponseEntity.ok(postService.updatePost(userDetails.getUser(), id, request));
    }

    // 4. 삭제 (Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/persona/{id}")
    public ResponseEntity<List<String>> getAllTextsByUserId(@PathVariable Long id){
        return ResponseEntity.ok(postService.getAllTextsByUserId(id));
    }
}