package com.project.byeoryback.domain.widget.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
public class WidgetRequest {
    private String name;    // 위젯 이름
    private String type;    // 위젯 타입 (book-info 등)
    private Map<String, Object> content; // 내용 (JSON)
    private Map<String, Object> styles;  // 스타일 (JSON)
}