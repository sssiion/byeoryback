package com.project.byeoryback.domain.setting.theme.controller;

import com.project.byeoryback.domain.setting.theme.dto.ThemeDto;
import com.project.byeoryback.domain.setting.theme.service.ThemeService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setting/theme")
@RequiredArgsConstructor
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping
    public ResponseEntity<ThemeDto> getTheme(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();

        return ResponseEntity.ok(themeService.getTheme(userId));
    }

    @PutMapping
    public ResponseEntity<Void> updateTheme(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ThemeDto themeDto) {
        Long userId = userDetails.getUser().getId();

        themeService.updateTheme(userId, themeDto);
        return ResponseEntity.ok().build();
    }
}