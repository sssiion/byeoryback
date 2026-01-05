package com.project.byeoryback.domain.room.controller;

import com.project.byeoryback.domain.room.dto.RoomJoinRequest;
import com.project.byeoryback.domain.room.dto.RoomMemberResponse;
import com.project.byeoryback.domain.room.dto.RoomRequest;
import com.project.byeoryback.domain.room.dto.RoomResponse;
import com.project.byeoryback.domain.room.service.RoomService;
import com.project.byeoryback.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.createRoom(userDetails.getUser(), request));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> joinRoom(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody(required = false) RoomJoinRequest request) {
        String password = (request != null) ? request.getPassword() : null;
        roomService.joinRoom(id, userDetails.getUser(), password);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoom(id));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<RoomMemberResponse>> getMembers(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getMembers(id));
    }
}
