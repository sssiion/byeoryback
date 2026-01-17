package com.project.byeoryback.domain.customtwidget.controller;

import com.project.byeoryback.domain.customtwidget.dto.CustomWidgetRequest;
import com.project.byeoryback.domain.customtwidget.entity.CustomWidget;
import com.project.byeoryback.domain.customtwidget.service.CustomWidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.project.byeoryback.global.security.CustomUserDetails;

@RestController
@RequestMapping("/api/widgets")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // 프론트엔드 포트 허용
public class CustomWidgetController {

    private final CustomWidgetService customWidgetService;

    /**
     * 1. 위젯 생성 (내 보관함에 저장)
     * POST /api/widgets
     * Header: X-User-Id (로그인한 유저 ID)
     */
    @PostMapping
    public ResponseEntity<CustomWidget> createWidget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CustomWidgetRequest request) {
        return ResponseEntity.ok(customWidgetService.createWidget(userDetails.getUser().getId(), request));
    }

    /**
     * 2. 내 위젯 목록 조회 (마이페이지용)
     * GET /api/widgets/my?page=0&size=10
     * Header: X-User-Id
     */
    @GetMapping("/my")
    public ResponseEntity<Page<CustomWidget>> getMyWidgets(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(customWidgetService.getMyWidgets(userDetails.getUser().getId(), pageable));
    }

    /**
     * 3. 위젯 마켓 목록 조회 (공유된 위젯들)
     * GET /api/widgets/market?page=0&size=10
     * (로그인 안 해도 볼 수 있다고 가정 -> Header 필수 아님)
     */
    @GetMapping("/market")
    public ResponseEntity<Page<CustomWidget>> getSharedWidgets(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(customWidgetService.getSharedWidgets(pageable));
    }

    /**
     * 4. 위젯 상세 조회
     * GET /api/widgets/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomWidget> getWidget(@PathVariable Long id) {
        return ResponseEntity.ok(customWidgetService.getWidget(id));
    }

    /**
     * 5. 위젯 수정
     * PUT /api/widgets/{id}
     * Header: X-User-Id (본인 확인용 - Service에서 검증 로직 추가 권장)
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomWidget> updateWidget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id,
            @RequestBody CustomWidgetRequest request) {
        return ResponseEntity.ok(customWidgetService.updateWidget(id, request));
    }

    /**
     * 6. 위젯 삭제
     * DELETE /api/widgets/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWidget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        customWidgetService.deleteWidget(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 7. 공유 상태 토글 (공유하기 / 공유취소)
     * PATCH /api/widgets/{id}/share
     * Header: X-User-Id
     */
    @PatchMapping("/{id}/share")
    public ResponseEntity<CustomWidget> toggleShare(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        return ResponseEntity.ok(customWidgetService.toggleShare(userDetails.getUser().getId(), id));
    }

    /**
     * 8. 위젯 가져오기 (Fork) - 남의 위젯 복사해서 내 것으로 만들기
     * POST /api/widgets/{id}/fork
     * Header: X-User-Id (복사해가는 사람 ID)
     */
    @PostMapping("/{id}/fork")
    public ResponseEntity<CustomWidget> forkWidget(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable("id") Long originalWidgetId) {
        return ResponseEntity.ok(customWidgetService.forkWidget(userDetails.getUser().getId(), originalWidgetId));
    }
}