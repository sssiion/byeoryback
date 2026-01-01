package com.project.byeoryback.domain.pin.controller;

import com.project.byeoryback.domain.pin.dto.PinRegisterRequest;
import com.project.byeoryback.domain.pin.dto.PinStatusResponse;
import com.project.byeoryback.domain.pin.dto.PinUnlockRequest;
import com.project.byeoryback.domain.pin.dto.PinVerifyRequest;
import com.project.byeoryback.domain.pin.service.PinService;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pin")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;
    private final UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<Void> registerPin(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PinRegisterRequest request) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        pinService.registerPin(user.getId(), request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyPin(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PinVerifyRequest request) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        boolean isValid = pinService.verifyPin(user.getId(), request);
        return ResponseEntity.ok(isValid);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkPinSet(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(pinService.checkPinSet(user.getId()));
    }

    @DeleteMapping
    public ResponseEntity<Void> deletePin(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        pinService.deletePin(user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<PinStatusResponse> getPinStatus(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return ResponseEntity.ok(pinService.getPinStatus(user.getId()));
    }

    @PostMapping("/unlock-request")
    public ResponseEntity<Void> requestUnlock(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        pinService.requestUnlock(user.getId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/unlock")
    public ResponseEntity<Void> unlockPin(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PinUnlockRequest request) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        pinService.unlockPin(user.getId(), request);
        return ResponseEntity.ok().build();
    }
}
