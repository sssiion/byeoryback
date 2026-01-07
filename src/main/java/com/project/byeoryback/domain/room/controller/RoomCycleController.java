package com.project.byeoryback.domain.room.controller;

import com.project.byeoryback.domain.room.dto.RoomCycleCreateRequest;
import com.project.byeoryback.domain.room.dto.RoomCycleResponse;
import com.project.byeoryback.domain.room.service.RoomCycleService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RoomCycleController {

    private final RoomCycleService roomCycleService;

    @PostMapping("/rooms/{roomId}/cycles")
    public ResponseEntity<RoomCycleResponse> createCycle(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RoomCycleCreateRequest request) {
        return ResponseEntity.status(201)
                .body(roomCycleService.createCycle(roomId, userDetails.getUser(), request));
    }

    @GetMapping("/rooms/{roomId}/cycles")
    public ResponseEntity<List<RoomCycleResponse>> getRoomCycles(
            @PathVariable Long roomId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(roomCycleService.getRoomCycles(roomId, userDetails.getUser()));
    }

    @GetMapping("/cycles/{cycleId}")
    public ResponseEntity<RoomCycleResponse> getCycle(
            @PathVariable Long cycleId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(roomCycleService.getCycle(cycleId, userDetails.getUser()));
    }

    @PostMapping("/cycles/{cycleId}/pass")
    public ResponseEntity<RoomCycleResponse> passTurn(
            @PathVariable Long cycleId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(roomCycleService.passTurn(cycleId, userDetails.getUser()));
    }

    @GetMapping("/cycles/{cycleId}/content")
    public ResponseEntity<com.project.byeoryback.domain.post.dto.PostResponse> getCycleContent(
            @PathVariable Long cycleId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(roomCycleService.getCycleContent(cycleId, userDetails.getUser()));
    }

    @PostMapping("/cycles/{cycleId}/content")
    public ResponseEntity<com.project.byeoryback.domain.post.dto.PostResponse> submitCycleContent(
            @PathVariable Long cycleId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody com.project.byeoryback.domain.post.dto.PostRequest request) {
        return ResponseEntity.ok(roomCycleService.submitCycleContent(cycleId, userDetails.getUser(), request));
    }
}
