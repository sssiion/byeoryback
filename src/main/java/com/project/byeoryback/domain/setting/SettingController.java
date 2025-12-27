package com.project.byeoryback.domain.setting;

import com.project.byeoryback.domain.setting.menu.service.MenuService;
import com.project.byeoryback.domain.setting.page.service.PageService;
import com.project.byeoryback.domain.setting.theme.service.ThemeService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/setting")
@RequiredArgsConstructor
public class SettingController {
    // 통합 조회용 (/api/setting/all)

    private final ThemeService themeService;
    private final MenuService menuService;
    private final PageService pageService;
    // private final WidgetService widgetService; // 추후 구현 시 주석 해제

    @GetMapping("/all")
    public ResponseEntity<AllSettingsResponse> getAllSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();

        return ResponseEntity.ok(new AllSettingsResponse(
                themeService.getTheme(userId),
                menuService.getMenu(userId),
                Collections.emptyList(), // widget (아직 구현 안됨)
                pageService.getPage(userId)));
    }
}
