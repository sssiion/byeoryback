package com.project.byeoryback.domain.persona.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PersonaResponse {
    private String analysisResult;
    private String emotionKeywords;
}
