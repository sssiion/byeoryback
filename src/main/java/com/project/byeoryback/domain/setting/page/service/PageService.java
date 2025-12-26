package com.project.byeoryback.domain.setting.page.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.byeoryback.domain.setting.page.dto.DefaultPageType;
import com.project.byeoryback.domain.setting.page.dto.PageDto;
import com.project.byeoryback.domain.setting.page.entity.PageSetting;
import com.project.byeoryback.domain.setting.page.repository.PageSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PageService {

    private final PageSettingRepository pageRepository;
    private final ObjectMapper objectMapper;

    // 기본값 설정 (/home)
    private static final PageDto DEFAULT_PAGE = new PageDto(DefaultPageType.HOME);

    @Transactional(readOnly = true)
    public PageDto getPage(Long userId) {
        return pageRepository.findByUserId(userId)
                .map(setting -> convertJsonToDto(setting.getPageConfig()))
                .orElse(DEFAULT_PAGE);
    }

    @Transactional
    public void updatePage(Long userId, PageDto pageDto) {
        String jsonConfig = convertDtoToJson(pageDto);

        try {
            PageSetting setting = pageRepository.findByUserId(userId)
                    .orElseGet(() -> PageSetting.builder().userId(userId).build());

            setting.updateConfig(jsonConfig);
            pageRepository.saveAndFlush(setting);

        } catch (DataIntegrityViolationException e) {
            log.warn("PageSetting 동시 요청 감지 (UserId: {}) - 업데이트로 전환", userId);
            // 동시성 문제 발생 시 재조회 후 업데이트
            PageSetting existing = pageRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("재조회 실패", e));
            existing.updateConfig(jsonConfig);
            pageRepository.save(existing);
        }
    }

    private PageDto convertJsonToDto(String json) {
        try {
            return objectMapper.readValue(json, PageDto.class);
        } catch (JsonProcessingException e) {
            log.error("페이지 설정 파싱 오류", e);
            return DEFAULT_PAGE;
        }
    }

    private String convertDtoToJson(PageDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("페이지 설정 저장 오류", e);
        }
    }
}
