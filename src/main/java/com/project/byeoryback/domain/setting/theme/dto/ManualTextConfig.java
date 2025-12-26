package com.project.byeoryback.domain.setting.theme.dto;

public record ManualTextConfig(

        // 수동 텍스트 설정

        String color,       // Hex Code (예: #1a1a1a)
        Integer intensity   // 0 ~ 100

) {}
