package com.project.byeoryback.domain.user.repository;

import com.project.byeoryback.domain.user.entity.Post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    // 특정 유저의 글 목록 조회 (최신순)
    List<Post> findAllByUserIdOrderByCreatedAtDesc(Long userId);
}