package com.project.byeoryback.domain.customtwidget.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class CustomWidgetRequest {
    private String name;
    private String type;
    private String defaultSize; // 🌟 [NEW] 사이즈 필드 추가 (2x2, 4x2 등)
    private Map<String, Object> content;
    private Map<String, Object> styles;
    private List<Map<String, Object>> decorations; // Decorations List
    private boolean isShared; // (선택) 공유 여부도 같이 받을 수 있음
}