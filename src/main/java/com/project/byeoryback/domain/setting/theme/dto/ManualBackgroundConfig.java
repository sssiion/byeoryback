package com.project.byeoryback.domain.setting.theme.dto;

public record ManualBackgroundConfig(

        // 배경 관련 세부 설정

        Boolean isGradient,         // 그라데이션 사용 여부
        String color,               // 색상(단색)
        Integer intensity,          // 밝기 세기

        String gradientDirection,   // 그라데이션 방향
        String gradientStart,       // 그라데이션 시작
        String gradientEnd,         // 그라데이션 끝

        String image,               // Base64 문자열(사진 좌표)
        String size                 // 배경 채우기, 반복

) {}
