package com.project.byeoryback.domain.setting.theme.service;

import com.project.byeoryback.domain.setting.theme.dto.*;
import com.project.byeoryback.domain.setting.theme.entity.ThemeSetting;
import com.project.byeoryback.domain.setting.theme.repository.ThemeSettingRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThemeService {

    private final ThemeSettingRepository themeRepository;
    private final ObjectMapper objectMapper;

    /**
     * 현재 테마 조회
     * 데이터가 없으면 기본(DEFAULT) 설정을 반환
     */
    @Transactional(readOnly = true)
    public ThemeDto getTheme(Long userId) {
        return themeRepository.findByUserId(userId)
                .map(setting -> convertJsonToDto(setting.getThemeConfig()))
                .orElseGet(this::getDefaultTheme);
    }

    /**
     * 테마 설정 업데이트 (없으면 생성, 있으면 수정)
     */
    @Transactional
    public void updateTheme(Long userId, ThemeDto themeDto) {
        // 1. 유효성 검사 (Manual인데 설정이 없는 경우 등)
        if (themeDto.mode() == ThemeMode.MANUAL && themeDto.manualConfig() == null) {
            throw new IllegalArgumentException("매뉴얼 모드에는 상세 설정이 필요합니다.");
        }

        // 2. DTO -> JSON String 변환
        String jsonConfig = convertDtoToJson(themeDto);

        // 3. 저장 또는 업데이트
        ThemeSetting setting = themeRepository.findByUserId(userId)
                .orElseGet(() -> ThemeSetting.builder().userId(userId).build());

        setting.updateConfig(jsonConfig);
        themeRepository.save(setting);
    }

    // --- Helper Methods ---

    private ThemeDto getDefaultTheme() {
        // DB에 값이 없을 때 나갈 기본값
        return new ThemeDto(
                ThemeMode.DEFAULT,
                new ThemeFontConfig("'Noto Sans KR', sans-serif", "16px"),
                null
        );
    }

    private ThemeDto convertJsonToDto(String json) {
        try {
            return objectMapper.readValue(json, ThemeDto.class);
        } catch (JsonProcessingException e) {
            log.error("JSON 파싱 에러", e);
            return getDefaultTheme(); // 에러 시 기본값 리턴 (안전장치)
        }
    }

    private String convertDtoToJson(ThemeDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("테마 설정 저장 중 오류 발생", e);
        }
    }
}