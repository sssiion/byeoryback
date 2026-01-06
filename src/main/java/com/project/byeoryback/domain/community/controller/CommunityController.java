package com.project.byeoryback.domain.community.controller;


import com.project.byeoryback.domain.community.dto.CommunityDto;
import com.project.byeoryback.domain.community.entity.Community;
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
    @GetMapping("/{communityId}")
    public ResponseEntity<CommunityDto.Response> getCommunity(@PathVariable Long communityId) {
        Community community = communityService.getCommunity(communityId);
        return ResponseEntity.ok(CommunityDto.Response.from(community));
    }

    // 필요하다면 전체 목록 조회, 정렬 등 추가 가능
}