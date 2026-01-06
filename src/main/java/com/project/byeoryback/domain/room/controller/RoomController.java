package com.project.byeoryback.domain.room.controller;

import com.project.byeoryback.domain.room.dto.RoomJoinRequest;
import com.project.byeoryback.domain.room.dto.RoomMemberResponse;
import com.project.byeoryback.domain.room.dto.RoomRequest;
import com.project.byeoryback.domain.room.dto.RoomResponse;
import com.project.byeoryback.domain.post.dto.PostResponse;
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

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }

    @GetMapping("/my")
    public ResponseEntity<List<RoomResponse>> getMyRooms(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(roomService.getMyJoinedRooms(userDetails.getUser()));
    }

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

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, userDetails.getUser(), request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoom(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoom(id));
    }

    @GetMapping("/{id}/members")
    public ResponseEntity<List<RoomMemberResponse>> getMembers(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getMembers(id));
    }

    @GetMapping("/{id}/posts")
    public ResponseEntity<List<PostResponse>> getPosts(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomPosts(id));
    }

    @DeleteMapping("/{id}/leave")
    public ResponseEntity<Void> leaveRoom(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        roomService.leaveRoom(id, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<Void> kickMember(
            @PathVariable Long id,
            @PathVariable Long userId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        roomService.kickMember(id, userId, userDetails.getUser());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        roomService.deleteRoom(id, userDetails.getUser());
        return ResponseEntity.ok().build();
    }
}
