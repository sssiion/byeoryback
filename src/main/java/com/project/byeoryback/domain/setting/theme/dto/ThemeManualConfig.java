package com.project.byeoryback.domain.setting.theme.dto;

public record ThemeManualConfig(

        // 수동 설정의 3가지 대분류를 묶어주는 래퍼(Wrapper)

        ManualTextConfig text,
        ManualBackgroundConfig background,
        ManualComponentConfig component

) {}
