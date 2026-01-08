package com.project.byeoryback.domain.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@Getter
@NoArgsConstructor
public class AnnualReportAiResponse {
    private String title;          // AI가 지어준 제목
    private String summary;        // 1년 총평
    private MusicRecommendation music; // 추천 BGM
    private List<ReportBlock> blocks;  // 본문 내용들

    @Getter
    @NoArgsConstructor
    public static class MusicRecommendation {
        private String title;
        private String youtubeUrl;
    }

    @Getter
    @NoArgsConstructor
    public static class ReportBlock {
        private String type;    // "text", "image", "timeline"
        private String content; // 텍스트 내용 or 이미지 URL
        private String date;    // "2024-05-05" (타임라인용)
        private String feeling; // "happy", "sad" 등 (타임라인용 감정 아이콘)
    }
}