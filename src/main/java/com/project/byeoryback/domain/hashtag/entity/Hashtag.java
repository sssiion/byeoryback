package com.project.byeoryback.domain.hashtag.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hashtags", indexes = {
        @Index(name = "idx_hashtag_name", columnList = "name", unique = true)
})
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
