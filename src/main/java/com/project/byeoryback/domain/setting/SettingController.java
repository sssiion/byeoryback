package com.project.byeoryback.domain.setting;

import com.project.byeoryback.domain.setting.menu.service.MenuService;
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
    // private final WidgetService widgetService; // 추후 구현 시 주석 해제
    // private final PageService pageService;     // 추후 구현 시 주석 해제

    @GetMapping("/all")
    public ResponseEntity<AllSettingsResponse> getAllSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getUser().getId();

        return ResponseEntity.ok(new AllSettingsResponse(
                // 1. Theme (완성됨)
                themeService.getTheme(userId),
                menuService.getMenu(userId).menuOrder(),

                // 3. Widget (아직 없음 -> 빈 리스트 반환)
                // widgetService.getWidgets(userId),
                Collections.emptyList(),

                // 4. Page (아직 없음 -> null 반환)
                // pageService.getPage(userId),
                Collections.emptyList()
        ));
    }
}
