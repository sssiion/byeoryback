package com.project.byeoryback.domain.setting.menu.controller;

import com.project.byeoryback.global.security.CustomUserDetails;
import com.project.byeoryback.domain.setting.menu.dto.MenuDto;
import com.project.byeoryback.domain.setting.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/setting/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<MenuDto> getMenu(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(menuService.getMenu(userDetails.getUser().getId()));
    }

    @PutMapping
    public ResponseEntity<Void> updateMenu(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MenuDto menuDto
    ) {
        menuService.updateMenu(userDetails.getUser().getId(), menuDto);
        return ResponseEntity.ok().build();
    }
}
