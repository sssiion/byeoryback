package com.project.byeoryback.domain.post.Persona.dto;


import lombok.Data;
import java.util.List;

@Data
public class UserPersonaAnalysisResponse {
    // 1. 페르소나/성격 분석 내용
    private PersonaAnalysis analysis;

    // 2. 많이 쓰인 단어 상위 10개 (동사, 형용사)
    private List<WordCount> topWords;

    @Data
    public static class PersonaAnalysis {
        private String summary;          // 전체적인 한 줄 요약
        private String personality;      // 성격적 특징
        private String emotionalState;   // 주된 감정 상태
        private String writingStyle;     // 글쓰기 스타일 (문체)
    }

    @Data
    public static class WordCount {
        private String word;  // 단어 (기본형, 예: '먹었다' -> '먹다')
        private String type;  // 품사 (동사 or 형용사)
        private int count;    // 등장 횟수
    }
}