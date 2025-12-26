package com.project.byeoryback.domain.setting.theme.dto;

public record ManualComponentConfig(

        // 카드 색상, 버튼 색상 등 세부 색상 설정

        String cardColor,   // 카드 색상
        String btnColor,    // 버튼 색상
        String btnTextColor // 버튼 글 색상

) {}
