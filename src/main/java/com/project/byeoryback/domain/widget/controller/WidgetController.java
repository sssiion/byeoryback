package com.project.byeoryback.domain.widget.controller;


import com.project.byeoryback.domain.widget.entity.WidgetDefinition;
import com.project.byeoryback.domain.widget.repository.WidgetDefinitionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/widgets")
@RequiredArgsConstructor
public class WidgetController {

    private final WidgetDefinitionRepository widgetRepository;

    @GetMapping
    public ResponseEntity<List<WidgetDefinition>> getWidgets(@RequestParam(required = false) Long userId) {
        List<WidgetDefinition> widgets;
        if (userId != null) {
            widgets = widgetRepository.findByIsSystemTrueOrUserId(userId);
        } else {
            widgets = widgetRepository.findByIsSystemTrue();
        }
        return ResponseEntity.ok(widgets);
    }
}