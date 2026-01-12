package com.project.byeoryback.domain.setting.widget.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class WidgetPresetRequest {
    private String name;
    private List<Map<String, Object>> widgets;
    private Map<String, Integer> gridSize;
}
