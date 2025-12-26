package com.project.byeoryback.domain.setting.menu.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.byeoryback.domain.setting.menu.dto.MenuDto;
import com.project.byeoryback.domain.setting.menu.entity.MenuSetting;
import com.project.byeoryback.domain.setting.menu.repository.MenuSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuSettingRepository menuRepository;
    private final ObjectMapper objectMapper;

    // 기본 순서
    private static final List<String> DEFAULT_ORDER = List.of("/home", "/post", "/community", "/market");

    @Transactional(readOnly = true)
    public MenuDto getMenu(Long userId) {
        return menuRepository.findByUserId(userId)
                .map(setting -> convertJsonToDto(setting.getMenuConfig()))
                .orElseGet(() -> new MenuDto(DEFAULT_ORDER));
    }

    @Transactional
    public void updateMenu(Long userId, MenuDto menuDto) {
        String jsonConfig = convertDtoToJson(menuDto);

        MenuSetting setting = menuRepository.findByUserId(userId)
                .orElseGet(() -> MenuSetting.builder().userId(userId).build());

        setting.updateConfig(jsonConfig);
        menuRepository.save(setting);
    }

    // --- JSON 변환 로직 ---
    private MenuDto convertJsonToDto(String json) {
        try {
            return objectMapper.readValue(json, MenuDto.class);
        } catch (JsonProcessingException e) {
            log.error("메뉴 설정 파싱 에러", e);
            return new MenuDto(DEFAULT_ORDER);
        }
    }

    private String convertDtoToJson(MenuDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("메뉴 설정 저장 에러", e);
        }
    }
}
