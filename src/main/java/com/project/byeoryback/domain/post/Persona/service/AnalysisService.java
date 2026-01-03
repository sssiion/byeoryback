package com.project.byeoryback.domain.post.Persona.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.byeoryback.domain.post.Persona.dto.UserPersonaAnalysisResponse;
import com.project.byeoryback.domain.post.entity.Block;
import com.project.byeoryback.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisService {

    private final PostRepository postRepository;
    private final GeminiApiClient geminiApiClient; // Google API 통신용 컴포넌트
    private final ObjectMapper objectMapper; // JSON 파싱용

    @Transactional(readOnly = true)
    public UserPersonaAnalysisResponse analyzeUser(Long userId) {

        // 1. DB에서 텍스트 데이터만 싹 긁어오기 (이전 단계 코드 활용)
        List<List<Block>> allPostBlocks = postRepository.findBlocksByUserId(userId);

        String combinedText = allPostBlocks.stream()
                .filter(Objects::nonNull)
                .flatMap(List::stream)
                .map(Block::getText)
                .filter(text -> text != null && !text.isBlank())
                .collect(Collectors.joining("\n")); // 개행문자로 구분하여 합치기

        // 데이터가 너무 적으면 분석 불가 처리
        if (combinedText.length() < 50) {
            throw new RuntimeException("분석할 텍스트가 부족합니다.");
        }

        // 2. 프롬프트 구성
        String prompt = createPrompt(combinedText);

        // 3. Google AI Studio API 호출
        String jsonResponse = geminiApiClient.generateContent(prompt);

        // 4. 응답 JSON을 DTO로 변환
        try {
            // AI가 가끔 마크다운 ```json ... ``` 을 붙일 수 있으므로 제거 처리
            String cleanJson = jsonResponse.replace("```json", "").replace("```", "").trim();
            return objectMapper.readValue(cleanJson, UserPersonaAnalysisResponse.class);
        } catch (JsonProcessingException e) {
            log.error("AI 응답 파싱 실패: {}", jsonResponse);
            throw new RuntimeException("분석 결과를 처리하는 중 오류가 발생했습니다.");
        }
    }

    private String createPrompt(String userText) {
        return """
            너는 텍스트 데이터 분석 전문가야. 아래 제공된 [TEXT DATA]를 분석해서 JSON 결과를 줘.
            
            [요구사항]
            1. 텍스트에서 '동사'와 '형용사'만 추출하여 기본형(사전형)으로 변환 후 상위 10개 빈도수 계산.
            2. 텍스트 작성자의 성격, 감정, 문체를 분석.
            3. 응답은 오직 JSON 포맷만 반환.
            
            [JSON 구조 예시]
            {
              "analysis": { "summary": "...", "personality": "...", "emotionalState": "...", "writingStyle": "..." },
              "topWords": [ { "word": "가다", "type": "동사", "count": 5 } ]
            }

            [TEXT DATA]
            %s
            """.formatted(userText);
    }
}