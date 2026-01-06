package com.project.byeoryback.domain.post.repository;

import com.project.byeoryback.domain.post.entity.PostStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostStatRepository extends JpaRepository<PostStat, Long> {
    Optional<PostStat> findByPostId(Long postId);

    void deleteByPostId(Long postId);
}
