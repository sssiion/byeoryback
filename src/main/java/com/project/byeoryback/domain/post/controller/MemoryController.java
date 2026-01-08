package com.project.byeoryback.domain.post.controller;


import com.project.byeoryback.domain.post.dto.PostResponse;
import com.project.byeoryback.domain.post.entity.Post;
import com.project.byeoryback.domain.post.service.AnnualReportService;
import com.project.byeoryback.domain.post.service.PostService; // 1년 전 로직이 여기 있다면 주입 필요
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/memories") // URL을 'memories'로 분리하여 관리
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class MemoryController {

    private final AnnualReportService annualReportService; // AI 회고록 생성용
    private final PostService postService; // 1년 전 추억 조회용

    /**
     * 1. [AI] 연말정산 회고록 생성 및 저장
     * URL: POST /api/memories/annual-report?year=2024
     * 설명: 지정한 연도의 일기를 AI가 분석하여 새로운 '회고록 포스트'를 생성합니다.
     */
    @PostMapping("/annual-report")
    public ResponseEntity<?> generateAnnualReport(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam("year") int year) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // AI 분석 및 포스트 생성 서비스 호출
            Post reportPost = annualReportService.createAnnualReport(userDetails.getUser().getId(), year);

            // 생성된 포스트 반환 (프론트에서 바로 보여주기 위함)
            return ResponseEntity.ok(PostResponse.from(reportPost));

        } catch (IllegalArgumentException e) {
            // 데이터가 없는 경우 (200 OK지만 메시지를 담아 보냄 or 400 Bad Request)
            log.warn("회고록 생성 실패: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error("회고록 생성 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("회고록을 생성하는 중 문제가 발생했습니다. 잠시 후 다시 시도해주세요.");
        }
    }

    /**
     * 2. [추억] 1년 전 오늘의 가장 알찬 기록 가져오기
     * URL: GET /api/memories/today-last-year
     * 설명: 작년 오늘(없으면 그 달) 작성한 가장 긴/풍성한 일기를 하나 가져옵니다.
     */
    @GetMapping("/today-last-year")
    public ResponseEntity<?> getMemorablePost(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            // 이전에 만든 'getMemorablePostFromOneYearAgo' 메서드 호출
            Post memorablePost = postService.getMemorablePostFromOneYearAgo(userDetails.getUser().getId());

            return ResponseEntity.ok(PostResponse.from(memorablePost));

        } catch (IllegalStateException e) {
            // 3달을 뒤져도 데이터가 없는 경우 -> 프론트에서 띄울 메시지 전달
            // 204 No Content를 보내거나, 200 OK와 함께 메시지를 보낼 수 있음
            return ResponseEntity.status(HttpStatus.OK)
                    .body("아직 데이터가 없어요 ㅠㅠ 오늘부터 추억을 더 많이 쌓아봐요 !!");
        }
    }

    @GetMapping("/random")
    public ResponseEntity<?> getRandomPost(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Post randomPost = postService.findRandomPostByUserId(userDetails.getUser().getId());
            return ResponseEntity.ok(PostResponse.from(randomPost));

        } catch (IllegalArgumentException e) {
            // 작성한 글이 하나도 없을 때
            return ResponseEntity.status(HttpStatus.OK)
                    .body("아직 작성된 일기가 없어요! 첫 기록을 남겨보세요 🍀");
        } catch (Exception e) {
            log.error("랜덤 일기 조회 중 오류", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}