package com.project.byeoryback.domain.user.entity.Post;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

// 블록 (글, 이미지 등)
@Data
@NoArgsConstructor
public class Block {
    private String id;
    private String type; // paragraph, image-full 등
    private String text;
    private String imageUrl;  // 여기에 Supabase URL 등이 저장됨
    private String imageUrl2;
    private Integer imageRotation;
    private String imageFit;
    private Map<String, Object> styles;
}

