package com.project.byeoryback.domain.setting;

import com.project.byeoryback.domain.setting.menu.service.MenuService;
import com.project.byeoryback.domain.setting.page.service.PageService;
import com.project.byeoryback.domain.setting.theme.service.ThemeService;
import com.project.byeoryback.global.security.CustomUserDetails;
import com.project.byeoryback.domain.setting.widget.WidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private final WidgetService widgetService;
    private final com.project.byeoryback.domain.setting.header.HeaderService headerService;

    @GetMapping("/all")
    public ResponseEntity<AllSettingsResponse> getAllSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUser().getId();

        java.util.List<java.util.Map<String, Object>> widgets = widgetService.getWidgets(userId);
        return ResponseEntity.ok(new AllSettingsResponse(
                themeService.getTheme(userId),
                menuService.getMenu(userId),
                widgets != null ? Collections.unmodifiableList(widgets) : null,
                pageService.getPage(userId),
                com.project.byeoryback.domain.setting.header.HeaderDto.from(headerService.getHeaderSetting(userId))));
    }

    @PutMapping("/header")
    public ResponseEntity<Void> updateHeaderSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody com.project.byeoryback.domain.setting.header.HeaderDto dto) {
        headerService.updateHeaderSetting(userDetails.getUser().getId(), dto.showTimer(), dto.showCredit(),
                dto.showWidgetZoom());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/widgets")
    public ResponseEntity<Void> updateWidgetSettings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody java.util.List<java.util.Map<String, Object>> layoutData) {
        widgetService.updateWidgets(userDetails.getUser().getId(), layoutData);
        return ResponseEntity.ok().build();
    }

    // --- Preset Endpoints ---

    @GetMapping("/widgets/presets")
    public ResponseEntity<java.util.List<java.util.Map<String, Object>>> getPresets(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        // Convert Entity to simple JSON structure for frontend
        java.util.List<java.util.Map<String, Object>> result = widgetService.getPresets(userDetails.getUser().getId())
                .stream()
                .map(p -> {
                    java.util.Map<String, Object> map = new java.util.HashMap<>();
                    map.put("id", p.getId());
                    map.put("name", p.getName());
                    map.put("createdAt", java.time.format.DateTimeFormatter.ISO_DATE_TIME.format(p.getCreatedAt()));
                    map.put("widgets", p.getLayoutData());
                    map.put("gridSize", p.getGridSize());
                    return map;
                })
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @org.springframework.web.bind.annotation.PostMapping("/widgets/presets")
    public ResponseEntity<Void> createPreset(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody java.util.Map<String, Object> request) {

        String name = (String) request.get("name");
        java.util.List<java.util.Map<String, Object>> widgets = (java.util.List<java.util.Map<String, Object>>) request
                .get("widgets");
        java.util.Map<String, Integer> gridSize = (java.util.Map<String, Integer>) request.get("gridSize");

        widgetService.createPreset(userDetails.getUser().getId(), name, widgets, gridSize);
        return ResponseEntity.ok().build();
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/widgets/presets/{id}")
    public ResponseEntity<Void> deletePreset(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @org.springframework.web.bind.annotation.PathVariable Long id) {
        widgetService.deletePreset(userDetails.getUser().getId(), id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/widgets/presets/{id}")
    public ResponseEntity<Void> updatePreset(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @org.springframework.web.bind.annotation.PathVariable Long id,
            @RequestBody java.util.Map<String, Object> request) {

        java.util.List<java.util.Map<String, Object>> widgets = (java.util.List<java.util.Map<String, Object>>) request
                .get("widgets");
        java.util.Map<String, Integer> gridSize = (java.util.Map<String, Integer>) request.get("gridSize");

        widgetService.updatePreset(userDetails.getUser().getId(), id, widgets, gridSize);
        return ResponseEntity.ok().build();
    }
}
