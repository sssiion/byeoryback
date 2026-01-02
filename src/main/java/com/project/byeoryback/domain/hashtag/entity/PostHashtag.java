package com.project.byeoryback.domain.hashtag.entity;

import com.project.byeoryback.domain.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_hashtags", indexes = {
        @Index(name = "idx_post_hashtag", columnList = "hashtag_id, post_id")
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PostHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id", nullable = false)
    private Hashtag hashtag;
}
