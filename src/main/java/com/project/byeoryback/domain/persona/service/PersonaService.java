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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PersonaService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PersonaRepository personaRepository;
    private final GeminiService geminiService;
    private final ObjectMapper objectMapper;

    @Transactional
    public void analyzePersona(Long userId) {
        log.info("Fetching posts for user ID: {}", userId);

        // 1. Fetch all posts for the user
        List<Post> posts = postRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
        log.info("Found {} posts for user {}", posts.size(), userId);

        if (posts.isEmpty()) {
            throw new IllegalArgumentException("No posts found to analyze");
        }

        // 2. Concatenate post content
        StringBuilder sb = new StringBuilder();
        for (Post post : posts) {

            if (post.getTitle() != null) {
                sb.append("Title: ").append(post.getTitle()).append("\n");
            }
            if (post.getBlocks() != null) {
                post.getBlocks().forEach(block -> {

                    if (block.getText() != null) {
                        sb.append(block.getText()).append(" ");
                    }
                });
            }
            if (post.getFloatingTexts() != null) {
                post.getFloatingTexts().forEach(item -> {
                    if (item.getText() != null) {
                        sb.append(item.getText()).append(" ");
                    }
                });
            }
            sb.append("\n---\n");
        }

        String allContent = sb.toString();
        log.info("Constructed prompt content length: {}", allContent.length());

        // 3. Send to AI service for analysis
        // 3. Send to AI service for analysis
        String prompt = "Analyze the following user posts to create a 'Persona Emotion Card'.\n" +
                "Extract the user's persona, dominant emotions, and 5 keywords.\n" +
                "IMPORTANT: If the provided text is too short (less than 50 characters) or insufficient to analyze, simply set 'analysisResult' to '게시물을 더 작성해주세요' and 'emotionKeywords' to '분석 불가'. DO NOT improvise.\n"
                +
                "Otherwise, provide the analysis in KOREAN (한국어).\n" +
                "Return valid JSON only, without markdown formatting like ```json or ```.\n" +
                "Format:\n" +
                "{\n" +
                "  \"analysisResult\": \"사용자의 페르소나 분석 결과 (한국어)\",\n" +
                "  \"emotionKeywords\": \"감정 키워드 5개 (쉼표로 구분, 한국어)\"\n" +
                "}\n\n" +
                "User Posts:\n" +
                allContent;

        try {
            String jsonResponse = geminiService.analyzeText(prompt);
            log.info("Gemini Raw Response: {}", jsonResponse);

            if (jsonResponse != null) {
                // Clean up markdown if present
                jsonResponse = jsonResponse.replace("```json", "").replace("```", "").trim();

                JsonNode rootNode = objectMapper.readTree(jsonResponse);
                String analysisResult = rootNode.path("analysisResult").asText();
                String emotionKeywords = rootNode.path("emotionKeywords").asText();

                if (analysisResult.isEmpty() && emotionKeywords.isEmpty()) {
                    log.warn("Parsed fields are empty. Check AI response format.");
                }

                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new IllegalArgumentException("User not found"));

                // 4. Save result as Persona entity
                // Check if persona already exists
                String finalAnalysisResult = analysisResult;
                String finalEmotionKeywords = emotionKeywords;

                Persona persona = personaRepository.findByUserId(userId)
                        .map(p -> Persona.builder()
                                .id(p.getId())
                                .user(user)
                                .analysisResult(finalAnalysisResult)
                                .emotionKeywords(finalEmotionKeywords)
                                .createdAt(java.time.LocalDateTime.now())
                                .build())
                        .orElse(Persona.builder()
                                .user(user)
                                .analysisResult(finalAnalysisResult)
                                .emotionKeywords(finalEmotionKeywords)
                                .createdAt(java.time.LocalDateTime.now())
                                .build());

                personaRepository.save(persona);
            } else {
                log.warn("Gemini response is null");
            }
        } catch (Exception e) {
            log.error("Error analyzing persona", e);
            throw new RuntimeException("Failed to analyze persona", e);
        }
    }
}
