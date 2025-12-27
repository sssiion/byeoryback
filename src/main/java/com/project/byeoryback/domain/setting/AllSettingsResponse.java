package com.project.byeoryback.domain.setting;

import com.project.byeoryback.domain.setting.theme.dto.ThemeDto;
import com.project.byeoryback.domain.setting.menu.dto.MenuDto;
import com.project.byeoryback.domain.setting.page.dto.PageDto;

import java.util.List;

public record AllSettingsResponse(
                ThemeDto theme,
                MenuDto menu,
                List<Object> widget,
                PageDto page) {
}
