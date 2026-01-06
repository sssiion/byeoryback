package com.project.byeoryback.global.init;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.byeoryback.domain.market.entity.MarketItem;
import com.project.byeoryback.domain.market.entity.MarketItemStatus;
import com.project.byeoryback.domain.market.repository.MarketItemRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MarketDataInitializer {

        private final MarketItemRepository marketItemRepository;
        private final ObjectMapper objectMapper;

        @Bean
        public CommandLineRunner initMarketData() {
                return args -> {
                        log.info("Initializing basic market data...");

                        List<Map<String, Object>> initialItems = Arrays.asList(
                                        Map.of(
                                                        "referenceId", "pack_basic",
                                                        "category", "sticker",
                                                        "name", "⭐ 스타터 팩",
                                                        "description", "다이어리 꾸미기의 기본! 필수 스티커 10종 모음.",
                                                        "price", 500L,
                                                        "tags", Arrays.asList("기본", "스타터", "필수"),
                                                        "imageUrl",
                                                        "https://cdn-icons-png.flaticon.com/512/169/169367.png"),
                                        Map.of(
                                                        "referenceId", "sticker_pack_001",
                                                        "category", "sticker",
                                                        "name", "귀여운 고양이 팩",
                                                        "description", "다이어리에 쓰기 좋은 귀여운 고양이 스티커 모음입니다.",
                                                        "price", 1500L,
                                                        "tags", Arrays.asList("귀여움", "고양이", "동물"),
                                                        "imageUrl",
                                                        "https://cdn-icons-png.flaticon.com/512/616/616408.png"),
                                        Map.of(
                                                        "referenceId", "sticker_pack_002",
                                                        "category", "sticker",
                                                        "name", "빈티지 라벨",
                                                        "description", "감성적인 다꾸를 위한 빈티지 라벨 스티커입니다.",
                                                        "price", 1200L,
                                                        "tags", Arrays.asList("빈티지", "라벨", "브라운"),
                                                        "imageUrl",
                                                        "https://cdn-icons-png.flaticon.com/512/2361/2361730.png"),
                                        Map.of(
                                                        "referenceId", "widget_template_001",
                                                        "category", "template_widget",
                                                        "name", "미니멀 시계 & 투두",
                                                        "description", "책상 위를 깔끔하게 정리해주는 미니멀 위젯 세트입니다.",
                                                        "price", 3000L,
                                                        "tags", Arrays.asList("미니멀", "위젯", "생산성"),
                                                        "imageUrl",
                                                        "https://cdn-icons-png.flaticon.com/512/12117/12117188.png"),
                                        Map.of(
                                                        "referenceId", "post_template_001",
                                                        "category", "template_post",
                                                        "name", "공부 기록 템플릿",
                                                        "description", "오늘의 공부 시간을 기록하고 회고할 수 있는 템플릿입니다.",
                                                        "price", 2000L,
                                                        "tags", Arrays.asList("공부", "다이어리", "템플릿"),
                                                        "imageUrl",
                                                        "https://cdn-icons-png.flaticon.com/512/4021/4021693.png"),
                                        Map.of(
                                                        "referenceId", "sticker_pack_003",
                                                        "category", "sticker",
                                                        "name", "픽셀 아트 이모지",
                                                        "description", "레트로 게임 감성의 픽셀 아트 이모지 팩!",
                                                        "price", 1800L,
                                                        "tags", Arrays.asList("픽셀", "레트로", "이모지"),
                                                        "imageUrl",
                                                        "https://cdn-icons-png.flaticon.com/512/10603/10603762.png"));

                        for (Map<String, Object> data : initialItems) {
                                String referenceId = (String) data.get("referenceId");
                                String contentJson = objectMapper.writeValueAsString(Map.of(
                                                "description", data.get("description"),
                                                "imageUrl", data.get("imageUrl"),
                                                "tags", data.get("tags")));

                                MarketItem item;
                                if (marketItemRepository.existsByReferenceId(referenceId)) {
                                        // Assuming we need to fetch it to update.
                                        // Since we don't have findByReferenceId exposed as Optional in the interface
                                        // visible here (it might be, but let's be safe),
                                        // and strict replacement is hard without seeing the Repo again.
                                        // BUT I checked Repo and I ADDED findByReferenceId.
                                        // So I can use it!
                                        item = marketItemRepository.findByReferenceId(referenceId).orElse(null);
                                } else {
                                        item = MarketItem.builder()
                                                        .name((String) data.get("name"))
                                                        .price((Long) data.get("price"))
                                                        .category((String) data.get("category"))
                                                        .referenceId(referenceId)
                                                        .status(MarketItemStatus.ON_SALE)
                                                        .createdAt(LocalDateTime.now())
                                                        .build();
                                }

                                if (item != null) {
                                        item.setContentJson(contentJson);
                                        item.setName((String) data.get("name")); // Also update name in case translation
                                                                                 // changed
                                        marketItemRepository.save(item);
                                }
                        }
                        log.info("Basic market data initialization check completed.");
                };
        }
}
