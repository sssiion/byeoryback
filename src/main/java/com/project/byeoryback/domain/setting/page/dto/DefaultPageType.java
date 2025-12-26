package com.project.byeoryback.domain.setting.page.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public enum DefaultPageType {
    HOME("/home"),
    POST("/post"),
    COMMUNITY("/community"),
    MARKET("/market");

    private final String path;

    // 1. Java -> JSON (직렬화): "/home" 문자열로 나감
    @JsonValue
    public String getPath() {
        return path;
    }

    // 2. JSON -> Java (역직렬화): "/home" 들어오면 HOME Enum으로 변환
    @JsonCreator
    public static DefaultPageType from(String value) {
        return Arrays.stream(DefaultPageType.values())
                .filter(type -> type.path.equals(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("허용되지 않은 페이지 경로입니다: " + value));
    }
}