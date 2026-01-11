package com.project.byeoryback.domain.setting;

import com.project.byeoryback.domain.setting.theme.dto.ThemeDto;
import com.project.byeoryback.domain.setting.menu.dto.MenuDto;
import com.project.byeoryback.domain.setting.page.dto.PageDto;

import java.util.List;

import java.util.Map;

public record AllSettingsResponse(
        ThemeDto theme,
        MenuDto menu,
        List<Map<String, Object>> widget,
        PageDto page) {
}
