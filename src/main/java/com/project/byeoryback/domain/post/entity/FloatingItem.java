package com.project.byeoryback.domain.post.entity;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

// 스티커, 자유이미지 공통 부모 클래스 (필요 시)
@Data
@NoArgsConstructor
public class FloatingItem {
    private String id;
    private Object x;
    private Object y;
    private Object w;
    private Object h;
    private double rotation;

    @JsonProperty("zIndex")
    private int zIndex;
    private Double opacity = 1.0;
    private String url; // 이미지 URL
    private String text; // 텍스트 내용 (FloatingText용)
    private Map<String, Object> styles; // 스타일

    @Column(columnDefinition = "json") // JPA 환경에 따라 어노테이션 필요할 수 있음
    private Map<String, Object> widgetProps;
}