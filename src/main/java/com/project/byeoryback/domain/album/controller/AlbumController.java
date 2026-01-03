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

    @GetMapping
    public ResponseEntity<List<com.project.byeoryback.domain.album.dto.AlbumResponse>> getAllAlbums(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(albumService.getAllAlbums(userDetails.getUser().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<com.project.byeoryback.domain.album.dto.AlbumResponse> getAlbumById(@PathVariable Long id) {
        return ResponseEntity.ok(albumService.getAlbumById(id));
    }

    @PostMapping
    public ResponseEntity<com.project.byeoryback.domain.album.dto.AlbumResponse> createAlbum(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody com.project.byeoryback.domain.album.dto.AlbumRequest request) {
        return ResponseEntity.ok(albumService.createAlbum(userDetails.getUser(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<com.project.byeoryback.domain.album.dto.AlbumResponse> updateAlbum(
            @PathVariable Long id,
            @RequestBody com.project.byeoryback.domain.album.dto.AlbumRequest request) {
        return ResponseEntity.ok(albumService.updateAlbum(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/manage")
    public ResponseEntity<Void> manageAlbumContent(
            @PathVariable Long id,
            @RequestBody com.project.byeoryback.domain.album.dto.AlbumManageRequest request) {

        String action = request.getAction();
        Long contentId = request.getContentId();
        String typeStr = request.getType();

        if (contentId == null || typeStr == null || action == null) {
            throw new IllegalArgumentException("contentId, type, and action are required");
        }

        AlbumContent.ContentType type = AlbumContent.ContentType.valueOf(typeStr);

        if ("ADD".equalsIgnoreCase(action)) {
            albumService.addContentToAlbum(id, contentId, type);
        } else if ("REMOVE".equalsIgnoreCase(action)) {
            albumService.removeContentFromAlbum(id, contentId, type);
        } else if ("MOVE".equalsIgnoreCase(action)) {
            Long sourceAlbumId = request.getSourceAlbumId();
            if (sourceAlbumId == null) {
                throw new IllegalArgumentException("sourceAlbumId is required for MOVE action");
            }
            albumService.moveContent(id, sourceAlbumId, contentId, type);
        }

        return ResponseEntity.ok().build();
    }
}
