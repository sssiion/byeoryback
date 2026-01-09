package com.project.byeoryback.domain.persona.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PersonaSettingsRequest {
    private List<String> excludedHashtags;
}
