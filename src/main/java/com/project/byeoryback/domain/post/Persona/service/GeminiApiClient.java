package com.project.byeoryback.domain.post.Persona.service;

import com.project.byeoryback.domain.post.Persona.dto.GeminiMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeminiApiClient {

    private final WebClient.Builder webClientBuilder;

    @Value("${google.ai.api-key}")
    private String apiKey;

    @Value("${google.ai.model}")
    private String model; // 예: gemini-1.5-flash

    // Google Gemini API 호출 메서드
    public String generateContent(String promptText) {
        String url = "https://generativelanguage.googleapis.com/v1beta/models/" + model + ":generateContent?key=" + apiKey;

        // 1. 요청 객체 생성
        GeminiMessage.Part part = new GeminiMessage.Part(promptText);
        GeminiMessage.Content content = new GeminiMessage.Content(Collections.singletonList(part));
        GeminiMessage.Request request = new GeminiMessage.Request(Collections.singletonList(content));

        // 2. API 호출 (WebClient 사용)
        GeminiMessage.Response response = webClientBuilder.build()
                .post()
                .uri(url)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GeminiMessage.Response.class)
                .block(); // 동기식 처리를 위해 block() 사용 (필요시 비동기로 변경 가능)

        // 3. 응답 추출 및 예외 처리
        if (response != null && response.getCandidates() != null && !response.getCandidates().isEmpty()) {
            String rawText = response.getCandidates().get(0).getContent().getParts().get(0).getText();
            return cleanJson(rawText);
        } else {
            throw new RuntimeException("Gemini API 응답이 비어있습니다.");
        }
    }

    // 마크다운 코드 블록 제거 메서드 (```json ... ``` 제거)
    private String cleanJson(String text) {
        if (text == null) return "";
        return text.replace("```json", "")
                .replace("```", "")
                .trim();
    }
}