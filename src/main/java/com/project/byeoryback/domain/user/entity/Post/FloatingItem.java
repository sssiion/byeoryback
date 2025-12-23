package com.project.byeoryback.domain.user.entity.Post;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// 스티커, 자유이미지 공통 부모 클래스 (필요 시)
@Data
@NoArgsConstructor
public class FloatingItem {
    private String id;
    private double x;
    private double y;
    private double w;
    private double h;
    private double rotation;
    private int zIndex;
    private double opacity;
    private String url; // 이미지 URL
    private String text; // 텍스트 내용 (FloatingText용)
    private Map<String, Object> styles; // 스타일
}