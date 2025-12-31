package com.project.byeoryback.domain.auth.controller;

import com.project.byeoryback.domain.auth.dto.PinRequest;
import com.project.byeoryback.domain.auth.dto.PinUnlockRequest;
import com.project.byeoryback.domain.auth.service.PinService;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pin")
@RequiredArgsConstructor
public class PinController {

    private final PinService pinService;
    private final UserRepository userRepository;

    // Helper to get User from UserDetails
    private User getUser(UserDetails userDetails) {
        return userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @PostMapping("/setup")
    public ResponseEntity<String> setupPin(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PinRequest request) {
        User user = getUser(userDetails);
        pinService.setupPin(user, request.getPin());
        return ResponseEntity.ok("PIN setup successful");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyPin(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PinRequest request) {
        User user = getUser(userDetails);
        try {
            boolean valid = pinService.verifyPin(user, request.getPin());
            if (valid) {
                return ResponseEntity.ok("PIN verified");
            } else {
                return ResponseEntity.status(401).body("Invalid PIN");
            }
        } catch (IllegalStateException e) {
            if (e.getMessage().contains("locked")) {
                return ResponseEntity.status(403).body(Map.of("message", "Account locked", "status", "LOCKED"));
            }
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/lock/verify")
    public ResponseEntity<String> verifyUnlockCode(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PinUnlockRequest request) {
        User user = getUser(userDetails);
        try {
            pinService.unlockWithCode(user, request.getCode());
            return ResponseEntity.ok("Account unlocked");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/lock/resend")
    public ResponseEntity<String> resendUnlockCode(@AuthenticationPrincipal UserDetails userDetails) {
        User user = getUser(userDetails);
        try {
            pinService.resendUnlockCode(user);
            return ResponseEntity.ok("Unlock code resent");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/toggle")
    public ResponseEntity<String> togglePin(@AuthenticationPrincipal UserDetails userDetails,
            @RequestParam boolean enable) {
        User user = getUser(userDetails);
        try {
            pinService.togglePin(user, enable);
            return ResponseEntity.ok("PIN " + (enable ? "enabled" : "disabled"));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/change")
    public ResponseEntity<String> changePin(@AuthenticationPrincipal UserDetails userDetails,
            @RequestBody PinRequest request) {
        User user = getUser(userDetails);
        if (user.isPinLocked()) {
            return ResponseEntity.status(403).body("Account is locked. Unlock first.");
        }
        // Change is essentially setup with new pin, but we might want to check if
        // enabled.
        pinService.setupPin(user, request.getPin());
        return ResponseEntity.ok("PIN changed");
    }
}
