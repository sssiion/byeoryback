package com.project.byeoryback.domain.post.controller;

import com.project.byeoryback.domain.post.dto.PostTemplateDto;
import com.project.byeoryback.domain.post.service.PostTemplateService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class PostTemplateController {

    private final PostTemplateService postTemplateService;

    @GetMapping("/my")
    public ResponseEntity<List<PostTemplateDto.Response>> getMyTemplates(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Boolean showHidden) {
        return ResponseEntity.ok(postTemplateService.getMyTemplates(userDetails.getUser().getId(), showHidden));
    }

    @PostMapping
    public ResponseEntity<PostTemplateDto.Response> createTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody PostTemplateDto.CreateRequest request) {
        return ResponseEntity.ok(postTemplateService.createTemplate(userDetails.getUser().getId(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostTemplateDto.Response> getTemplate(@PathVariable Long id) {
        return ResponseEntity.ok(postTemplateService.getTemplate(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        postTemplateService.deleteTemplate(userDetails.getUser().getId(), id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<Void> restoreTemplate(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long id) {
        postTemplateService.restoreTemplate(userDetails.getUser().getId(), id);
        return ResponseEntity.ok().build();
    }
}
