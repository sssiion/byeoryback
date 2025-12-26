package com.project.byeoryback.domain.setting.theme.dto;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ThemeMode {

    /*
    * 테마 모드 값
    * default - 기본테마
    * light - 라이트테마
    * dark - 다크테마
    * manual - 개인테마
    */

    DEFAULT("default"),
    LIGHT("light"),
    DARK("dark"),
    MANUAL("manual"),

    // 특수 문자열 매핑
    CUSTOM_PURPLE("custom-purple"),
    CUSTOM_GREEN("custom-green"),
    CUSTOM_BLUE("custom-blue"),
    CUSTOM_RED("custom-red"),
    CUSTOM_ORANGE("custom-orange"),
    CUSTOM_PINK("custom-pink"),
    CUSTOM_TEAL("custom-teal"),
    CUSTOM_INDIGO("custom-indigo"),
    CUSTOM_GRAY("custom-gray"),
    CUSTOM_SUNSET("custom-sunset"),
    CUSTOM_OCEAN("custom-ocean"),
    CUSTOM_FOREST("custom-forest");

    private final String key;

    ThemeMode(String key) {
        this.key = key;
    }

    // 직렬화 시(Java -> JSON) 이 값을 사용
    @JsonValue
    public String getKey() {
        return key;
    }
}
