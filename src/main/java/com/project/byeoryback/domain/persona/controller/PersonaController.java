package com.project.byeoryback.domain.persona.controller;

import com.project.byeoryback.domain.persona.dto.PersonaResponse;
import com.project.byeoryback.domain.persona.repository.PersonaRepository;
import com.project.byeoryback.domain.persona.service.PersonaService;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/persona")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;
    private final PersonaRepository personaRepository;
    private final UserRepository userRepository;

    @PostMapping("/analyze")
    public ResponseEntity<?> analyzePersona(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        personaService.analyzePersona(user.getId());
        return ResponseEntity.ok("Persona analysis completed");
    }

    @GetMapping
    public ResponseEntity<?> getPersona(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return personaRepository.findByUserId(user.getId())
                .<ResponseEntity<?>>map(persona -> ResponseEntity.ok(PersonaResponse.builder()
                        .analysisResult(persona.getAnalysisResult())
                        .emotionKeywords(persona.getEmotionKeywords())
                        .build()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
