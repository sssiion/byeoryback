package com.project.byeoryback.domain.post.repository;

import com.project.byeoryback.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 특정 유저의 글 목록 조회 (최신순)
    List<Post> findAllByUserIdOrderByCreatedAtDesc(Long userId);

    void deleteByUserId(Long userId);
}