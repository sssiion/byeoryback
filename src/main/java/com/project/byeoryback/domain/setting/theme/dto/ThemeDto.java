package com.project.byeoryback.domain.setting.theme.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public record ThemeDto(

        /*
         * 테마 설정 최상위 데이터 전송 객체
         * manualConfig(수동 설정 묶음)이 null인 경우 JSON에서 생략
         * 모든 설정을 묶어 최종적으로 프론트에서 받게 되는 모습
         */

        // 테마 mode
        ThemeMode mode,

        // 폰트 설정
        ThemeFontConfig font,

        // 개인설정 - null인 경우 JSON에서 배제
        @JsonInclude(JsonInclude.Include.NON_NULL)
        ThemeManualConfig manualConfig

) {}
