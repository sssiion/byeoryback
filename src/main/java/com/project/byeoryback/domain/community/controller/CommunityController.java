package com.project.byeoryback.domain.community.controller;

import com.project.byeoryback.domain.community.dto.CommunityDto;
import com.project.byeoryback.domain.community.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/communities")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    /**
     * 커뮤니티 글 상세 조회
     * (조회수 1 증가)
     */
    /**
     * 커뮤니티 글 상세 조회
     * (조회수 1 증가)
     */
    @GetMapping("/{postId}")
    public ResponseEntity<CommunityDto.Response> getCommunity(
            @PathVariable Long postId,
            @RequestParam(required = false) Long userId) {
        return ResponseEntity.ok(communityService.getCommunity(postId, userId));
    }

    /**
     * 커뮤니티 글 목록 조회 (페이징)
     * GET /api/communities?page=0&size=10
     */
    @GetMapping
    public ResponseEntity<org.springframework.data.domain.Page<CommunityDto.Response>> getCommunityList(
            @org.springframework.data.web.PageableDefault(size = 10, sort = "createdAt", direction = org.springframework.data.domain.Sort.Direction.DESC) org.springframework.data.domain.Pageable pageable,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String hashtag) { // [New] hashtag 파라미터 추가
        return ResponseEntity.ok(communityService.getCommunityList(pageable, userId, hashtag));
    }

    /**
     * 좋아요 토글
     * POST /api/communities/{communityId}/like
     */
    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long postId,
            @RequestParam Long userId) {
        communityService.toggleLike(postId, userId);
        return ResponseEntity.ok().build();
    }

    // 필요하다면 전체 목록 조회, 정렬 등 추가 가능
}