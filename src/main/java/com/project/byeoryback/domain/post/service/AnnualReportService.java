package com.project.byeoryback.domain.post.service;

import com.project.byeoryback.domain.persona.service.GeminiService;
import com.project.byeoryback.domain.post.dto.AnnualReportAiResponse;
import com.project.byeoryback.domain.post.entity.Block;
import com.project.byeoryback.domain.post.entity.Post;
import com.project.byeoryback.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AnnualReportService {

    private final PostRepository postRepository;
    private final GeminiService geminiService;



    @Transactional
    public Post createAnnualReport(Long userId, int year) {
        // 1. 데이터 조회 (생략 가능, 위와 동일)
        LocalDateTime start = LocalDateTime.of(year, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(year, 12, 31, 23, 59);
        List<Post> posts = postRepository.findByUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(userId, start, end);

        if (posts.isEmpty()) throw new IllegalArgumentException("데이터가 없습니다.");

        // 2. 프롬프트 생성
        String prompt = createSystemPrompt(posts, year);

        // 3. Gemini 호출
        AnnualReportAiResponse aiResponse = geminiService.getAnalyzedJson(prompt, AnnualReportAiResponse.class);

        // 4. Block 엔티티 변환 (핵심 부분)
        List<Block> newBlocks = convertToEntityBlocks(aiResponse);

        // 5. Post 생성 및 저장
        Post reportPost = Post.builder()
                .user(posts.get(0).getUser())
                .title(aiResponse.getTitle())
                .isPublic(false)
                // .bgmUrl(aiResponse.getMusic().getYoutubeUrl()) // Post에 bgmUrl 필드가 있다면
                .blocks(newBlocks)
                .build();

        return postRepository.save(reportPost);
    }

    // --- Private Helpers ---

    // [Step 1] 프롬프트 생성 (AI에게 JSON 형식을 명확히 요구)
    private String createSystemPrompt(List<Post> posts, int year) {
        StringBuilder sb = new StringBuilder();
        sb.append("사용자의 ").append(year).append("년 일기를 분석해 회고록을 작성해줘.\n");
        sb.append("!!! 중요 !!!\n");
        sb.append("반드시 아래 포맷과 정확히 일치하는 순수한 JSON 데이터만 응답하세요. 마크다운이나 설명 텍스트를 붙이지 마세요.\n");
        sb.append("{\n");
        sb.append("  \"title\": \"제목\",\n");
        sb.append("  \"summary\": \"1년 요약 편지\",\n");
        sb.append("  \"music\": { \"title\": \"노래\", \"youtubeUrl\": \"링크\" },\n");
        sb.append("  \"blocks\": [\n");
        sb.append("    { \"type\": \"timeline\", \"date\": \"2024-05-01\", \"content\": \"사건 요약\", \"feeling\": \"happy\" },\n");
        sb.append("    { \"type\": \"text\", \"content\": \"본문 내용\" },\n");
        sb.append("    { \"type\": \"image\", \"content\": \"IMAGE_URL\" }\n");
        sb.append("  ]\n");
        sb.append("}\n\n");
        sb.append("DIARY DATA:\n");

        for (Post p : posts) {
            sb.append("[").append(p.getCreatedAt().toLocalDate()).append("] ");
            // Block의 'text' 필드 추출
            String textContent = p.getBlocks() != null ?
                    p.getBlocks().stream()
                            .filter(b -> "paragraph".equals(b.getType())) // 텍스트만
                            .map(Block::getText) // Block.getText() 사용
                            .collect(Collectors.joining(" "))
                    : "";

            if (textContent.length() > 150) textContent = textContent.substring(0, 150);
            sb.append(textContent).append("\n");

            // 이미지 URL 추출 ('imageUrl' 사용)
            if (p.getBlocks() != null) {
                p.getBlocks().stream()
                        .filter(b -> b.getImageUrl() != null)
                        .forEach(b -> sb.append("(Image: ").append(b.getImageUrl()).append(")\n"));
            }
        }
        return sb.toString();
    }

    // [Step 2] AI DTO -> 실제 Block 엔티티 변환 (제공해주신 클래스 구조 반영)
    private List<Block> convertToEntityBlocks(AnnualReportAiResponse response) {
        List<Block> entityBlocks = new ArrayList<>();

        // 1. 서론 (Summary)
        entityBlocks.add(createTextBlock(response.getSummary(), null));

        // 2. 본문 및 타임라인
        for (AnnualReportAiResponse.ReportBlock aiBlock : response.getBlocks()) {
            if ("image".equals(aiBlock.getType())) {
                // 이미지 블록 생성
                entityBlocks.add(createImageBlock(aiBlock.getContent()));
            } else if ("timeline".equals(aiBlock.getType())) {
                // 타임라인 블록 생성 (date, feeling 필드 활용)
                Block timelineBlock = createTextBlock(aiBlock.getContent(), aiBlock.getDate());
                timelineBlock.setFeeling(aiBlock.getFeeling()); // 감정 아이콘 등 활용 가능

                // 스타일로 강조 (프론트엔드가 styles를 지원한다면)
                Map<String, Object> styles = new HashMap<>();
                styles.put("fontWeight", "bold");
                styles.put("color", "#FF6B6B"); // 포인트 컬러
                timelineBlock.setStyles(styles);

                entityBlocks.add(timelineBlock);
            } else {
                // 일반 텍스트
                entityBlocks.add(createTextBlock(aiBlock.getContent(), null));
            }
        }
        return entityBlocks;
    }

    // [Helper] 텍스트 블록 생성기
    private Block createTextBlock(String text, String date) {
        Block block = new Block();
        block.setId(UUID.randomUUID().toString()); // ID 생성
        block.setType("paragraph"); // 프론트에서 사용하는 텍스트 타입
        block.setText(text); // content가 아니라 text 필드 사용
        block.setDate(date); // 타임라인 날짜 매핑
        return block;
    }

    // [Helper] 이미지 블록 생성기
    private Block createImageBlock(String url) {
        Block block = new Block();
        block.setId(UUID.randomUUID().toString());
        block.setType("image-full"); // 프론트에서 사용하는 이미지 타입
        block.setImageUrl(url); // url 필드 매핑
        block.setImageFit("contain"); // 기본값 설정
        return block;
    }
}