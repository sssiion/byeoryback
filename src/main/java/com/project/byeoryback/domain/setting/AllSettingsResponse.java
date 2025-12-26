package com.project.byeoryback.domain.setting;

import com.project.byeoryback.domain.setting.theme.dto.ThemeDto;
// import com.project.byeoryback.domain.setting.menu.dto.MenuDto;    // 작업중
// import com.project.byeoryback.domain.setting.widget.dto.WidgetDto; // 작업중
// import com.project.byeoryback.domain.setting.page.dto.PageDto;     // 작업중

import java.util.List;

public record AllSettingsResponse(
        ThemeDto theme,

        // 작업중
        Object menu,
        Object widget,
        Object page
) {}
