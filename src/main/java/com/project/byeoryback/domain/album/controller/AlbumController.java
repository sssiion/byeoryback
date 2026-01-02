package com.project.byeoryback.domain.album.controller;

import com.project.byeoryback.domain.album.dto.AlbumContentResponseDto;
import com.project.byeoryback.domain.album.entity.AlbumContent;
import com.project.byeoryback.domain.album.service.AlbumService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;

    @GetMapping("/{id}/contents")
    public ResponseEntity<List<AlbumContentResponseDto>> getAlbumContents(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.getAlbumContents(id));
    }

    @GetMapping("/unclassified")
    public ResponseEntity<List<?>> getUnclassifiedPosts(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(albumService.getUnclassifiedPosts(userDetails.getUser().getId()));
    }

    @PostMapping("/{id}/manage")
    public ResponseEntity<Void> manageAlbumContent(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request) {

        String action = (String) request.get("action"); // "ADD" or "REMOVE"
        Long contentId = ((Number) request.get("contentId")).longValue();
        String typeStr = (String) request.get("type"); // "POST"

        AlbumContent.ContentType type = AlbumContent.ContentType.valueOf(typeStr);

        if ("ADD".equalsIgnoreCase(action)) {
            albumService.addContentToAlbum(id, contentId, type);
        } else if ("REMOVE".equalsIgnoreCase(action)) {
            albumService.removeContentFromAlbum(id, contentId, type);
        }

        return ResponseEntity.ok().build();
    }
}
