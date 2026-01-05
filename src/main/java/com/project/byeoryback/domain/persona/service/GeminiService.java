package com.project.byeoryback.domain.persona.service;

import com.project.byeoryback.domain.persona.dto.gemini.GeminiRequest;
import com.project.byeoryback.domain.persona.dto.gemini.GeminiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiService {

        @Value("${GEMINI_API_KEY:}")
        private String apiKey;

        private final WebClient.Builder webClientBuilder;

        private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemma-3-12b-it:generateContent";

        public String analyzeText(String prompt) {
                if (apiKey == null || apiKey.trim().isEmpty()) {
                        throw new IllegalStateException(
                                        "Gemini API Key is missing. Please set GEMINI_API_KEY in your .env file.");
                }
                WebClient webClient = webClientBuilder.build();

                GeminiRequest request = GeminiRequest.builder()
                                .contents(Collections.singletonList(
                                                GeminiRequest.Content.builder()
                                                                .parts(Collections.singletonList(
                                                                                GeminiRequest.Part.builder()
                                                                                                .text(prompt)
                                                                                                .build()))
                                                                .build()))
                                .build();

                GeminiResponse response = webClient.post()
                                .uri(GEMINI_API_URL + "?key=" + apiKey)
                                .bodyValue(request)
                                .retrieve()
                                .bodyToMono(GeminiResponse.class)
                                .block();

                if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
                        GeminiResponse.Candidate candidate = response.getCandidates().get(0);
                        if (candidate.getContent() != null && candidate.getContent().getParts() != null
                                        && !candidate.getContent().getParts().isEmpty()) {
                                return candidate.getContent().getParts().get(0).getText();
                        }
                }

                return null;
        }
}
