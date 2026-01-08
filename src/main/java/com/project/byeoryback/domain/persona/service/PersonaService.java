package com.project.byeoryback.domain.persona.service;

import com.project.byeoryback.domain.persona.entity.Persona;
import com.project.byeoryback.domain.persona.repository.PersonaRepository;
import com.project.byeoryback.domain.post.entity.Post;
import com.project.byeoryback.domain.post.repository.PostRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PersonaService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PersonaRepository personaRepository;
    private final GeminiService geminiService; // AI 연동 서비스
    private final ObjectMapper objectMapper;

    @Transactional
    public void analyzePersona(Long userId) {
        // 오늘 날짜를 기준으로 연/월을 계산해서 넘김
        LocalDate now = LocalDate.now();
        analyzePersona(userId, now.getYear(), now.getMonthValue());
    }

    /**
     * 페르소나 분석 (전체 또는 월별)
     * @param userId 사용자 ID
     * @param year 분석할 연도 (null이면 전체)
     * @param month 분석할 월 (null이면 전체)
     */
    @Transactional
    public void analyzePersona(Long userId, Integer year, Integer month) {
        log.info("Analyze Persona - User: {}, Year: {}, Month: {}", userId, year, month);

        // 1. 게시글 조회 (기간 필터링 적용)
        List<Post> posts;

        if (year != null && month != null) {
            // 해당 월의 1일 00:00:00 ~ 마지막 날 23:59:59 계산
            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDateTime startDateTime = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endDateTime = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);

            // [New] Repository 메서드 사용
            posts = postRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, startDateTime, endDateTime);
        } else {
            // 전체 조회
            posts = postRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        }

        if (posts.isEmpty()) {
            throw new IllegalArgumentException("분석할 게시글이 존재하지 않습니다.");
        }

        // 2. 게시글 내용 병합 (분석용 텍스트 생성)
        StringBuilder sb = new StringBuilder();
        for (Post post : posts) {
            if (post.getTitle() != null) sb.append("Title: ").append(post.getTitle()).append("\n");

            // 텍스트 블록
            if (post.getBlocks() != null) {
                post.getBlocks().forEach(block -> {
                    if (block.getText() != null) sb.append(block.getText()).append(" ");
                });
            }
            // 플로팅 텍스트
            if (post.getFloatingTexts() != null) {
                post.getFloatingTexts().forEach(item -> {
                    if (item.getText() != null) sb.append(item.getText()).append(" ");
                });
            }
            sb.append("\n---\n");
        }

        String allContent = sb.toString();

        // 텍스트가 너무 짧으면 분석 중단 (비용 절감)
        if (allContent.length() < 50) {
            throw new IllegalArgumentException("게시글 내용이 너무 적어 분석할 수 없습니다.");
        }

        // 3. AI 프롬프트 구성 (JSON 응답 요청)
        String prompt = "Analyze the following user posts to create a detailed 'Digital Persona' report.\n" +
                "Provide the output in strict JSON format (no markdown code blocks, no ```json wrapper).\n" +
                "\n" +
                "IMPORTANT INSTRUCTION:\n" +
                "1. All JSON keys must remain in English (e.g., 'digitalSelf', 'moods').\n" +
                "2. All JSON values (descriptions, keywords, mood names, word cloud text) MUST be in KOREAN (한국어).\n" +
                "\n" +
                "JSON Schema & Example (Values must be in Korean):\n" +
                "{{\n" +
                "  \"representativeEmoji\": \"🐱\", (Select ONE emoji that best represents the user's overall vibe. e.g. 🐱, 🌿, ☕, 📚, 🎸)\n" +
                "\n" +
                "  \"digitalSelf\": [\n" +
                "    \"당신은 일상의 작은 순간들 속에서 평화를 찾습니다\", \n" +
                "    \"가까운 사람들과의 깊은 유대감을 중요하게 생각합니다\", \n" +
                "    \"자연 속에서 사색하며 에너지를 얻는 편입니다\"\n" +
                "  ], (3 sentences describing the user's persona in Korean styled like 'You tend to...')\n" +
                "\n" +
                "  \"characteristics\": [\"호기심 많은\", \"자연을 사랑하는\", \"사색적인\", \"감사하는\", \"창의적인\"], (5 key adjectives in Korean)\n" +
                "\n" +
                "  \"moods\": [\n" +
                "    {\"mood\": \"평온함\", \"percentage\": 45, \"emoji\": \"😌\"},\n" +
                "    {\"mood\": \"행복\", \"percentage\": 30, \"emoji\": \"😊\"},\n" +
                "    {\"mood\": \"설렘\", \"percentage\": 15, \"emoji\": \"🤩\"}\n" +
                "    (Mood names must be in Korean. Total percentage must be 100%)\n" +
                "  ],\n" +
                "\n" +
                "  \"wordCloud\": [\n" +
                "    {\"text\": \"커피\", \"value\": 24},\n" +
                "    {\"text\": \"여행\", \"value\": 15},\n" +
                "    {\"text\": \"퇴근\", \"value\": 10}\n" +
                "    (Top 10-15 most frequent/meaningful nouns in Korean)\n" +
                "  ]\n" +
                "}\n\n" +
                "User Posts:\n" +
                allContent;

        try {
            // 4. Gemini API 호출
            String jsonResponse = geminiService.analyzeText(prompt);
            log.debug("Gemini Response: {}", jsonResponse);

            if (jsonResponse != null) {
                // Markdown 포맷 제거 (혹시 포함될 경우를 대비)
                jsonResponse = jsonResponse.replace("```json", "").replace("```", "").trim();

                // 5. JSON 파싱 및 데이터 검증
                JsonNode rootNode = objectMapper.readTree(jsonResponse);

                // (선택) emotionKeywords 컬럼에 넣을 데이터 추출 (characteristics 배열 -> 쉼표 구분 문자열)
                StringBuilder keywordsBuilder = new StringBuilder();
                if (rootNode.has("characteristics") && rootNode.get("characteristics").isArray()) {
                    rootNode.get("characteristics").forEach(k -> keywordsBuilder.append(k.asText()).append(", "));
                }
                String keywordsStr = keywordsBuilder.toString();
                if (keywordsStr.endsWith(", ")) {
                    keywordsStr = keywordsStr.substring(0, keywordsStr.length() - 2);
                }

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

                // 6. DB 저장 (Update or Insert)
                // analysisResult 컬럼에 '전체 JSON 문자열'을 저장합니다.
                // 프론트엔드는 이 JSON을 파싱하여 차트와 카드를 그립니다.

                String finalJsonResult = jsonResponse;
                String finalKeywords = keywordsStr.isEmpty() ? "분석 불가" : keywordsStr;

                Persona persona = personaRepository.findByUserId(userId)
                        .map(p -> Persona.builder()
                                .id(p.getId()) // 기존 ID 유지
                                .user(user)
                                .analysisResult(finalJsonResult) // JSON 통째로 저장
                                .emotionKeywords(finalKeywords)  // 검색용 키워드 저장
                                .createdAt(LocalDateTime.now())
                                .build())
                        .orElse(Persona.builder()
                                .user(user)
                                .analysisResult(finalJsonResult)
                                .emotionKeywords(finalKeywords)
                                .createdAt(LocalDateTime.now())
                                .build());

                personaRepository.save(persona);
                log.info("Persona analysis saved for user {}", userId);
            } else {
                log.warn("Gemini response is null");
                throw new RuntimeException("AI 분석 응답이 없습니다.");
            }
        } catch (Exception e) {
            log.error("Error analyzing persona", e);
            throw new RuntimeException("AI 분석 중 오류가 발생했습니다.", e);
        }
    }
}