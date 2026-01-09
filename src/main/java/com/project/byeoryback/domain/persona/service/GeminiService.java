package com.project.byeoryback.domain.persona.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        private final ObjectMapper objectMapper; // JSON 변환을 위해 주입

        private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemma-3-12b-it:generateContent";

        public String analyzeText(String prompt) {
                if (apiKey == null || apiKey.trim().isEmpty()) {
                        throw new IllegalStateException(
                                        "Gemini API Key is missing. Please set GEMINI_API_KEY in your .env file.");
                }
                WebClient webClient = webClientBuilder
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                        .build();

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
                                .retryWhen(reactor.util.retry.Retry.backoff(3, java.time.Duration.ofSeconds(2))
                                        .filter(throwable -> throwable instanceof org.springframework.web.reactive.function.client.WebClientResponseException.ServiceUnavailable))
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
    // 2. [추가] JSON 파싱 메서드 (제네릭 사용)
    public <T> T getAnalyzedJson(String prompt, Class<T> responseType) {
        String jsonResponse = analyzeText(prompt);

        if (jsonResponse == null) {
            throw new RuntimeException("Gemini API 응답이 없습니다.");
        }

        // AI가 가끔 마크다운 코드블록(```json ... ```)을 포함해서 주므로 제거
        String cleanedJson = jsonResponse.replace("```json", "").replace("```", "").trim();

        // 중괄호 앞뒤의 불필요한 텍스트 제거 (안전장치)
        int startIndex = cleanedJson.indexOf("{");
        int endIndex = cleanedJson.lastIndexOf("}");
        if (startIndex != -1 && endIndex != -1) {
            cleanedJson = cleanedJson.substring(startIndex, endIndex + 1);
        }

        try {
            return objectMapper.readValue(cleanedJson, responseType);
        } catch (JsonProcessingException e) {
            log.error("JSON Parsing Error: {}", cleanedJson); // 에러 발생 시 원본 확인
            throw new RuntimeException("AI 응답을 객체로 변환하는데 실패했습니다.", e);
        }
    }
}
