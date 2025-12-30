package com.project.byeoryback.domain.widget.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
public class WidgetRequest {
    private String name;
    private String type;
    private String defaultSize; // 🌟 [NEW] 사이즈 필드 추가 (2x2, 4x2 등)
    private Map<String, Object> content;
    private Map<String, Object> styles;
    private boolean isShared; // (선택) 공유 여부도 같이 받을 수 있음
}