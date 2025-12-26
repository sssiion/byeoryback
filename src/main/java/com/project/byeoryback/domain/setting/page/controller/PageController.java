package com.project.byeoryback.domain.setting.page.controller;

import com.project.byeoryback.global.security.CustomUserDetails;
import com.project.byeoryback.domain.setting.page.dto.PageDto;
import com.project.byeoryback.domain.setting.page.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setting/page")
@RequiredArgsConstructor
public class PageController {

    private final PageService pageService;

    @GetMapping
    public ResponseEntity<PageDto> getPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(pageService.getPage(userDetails.getUser().getId()));
    }

    @PutMapping
    public ResponseEntity<Void> updatePage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PageDto pageDto
    ) {
        pageService.updatePage(userDetails.getUser().getId(), pageDto);
        return ResponseEntity.ok().build();
    }
}
