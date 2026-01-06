package com.project.byeoryback.domain.post.repository;

import com.project.byeoryback.domain.post.entity.Post;
import com.project.byeoryback.domain.post.entity.PostLike;
import com.project.byeoryback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserAndPost(User user, Post post);

    void deleteByUserAndPost(User user, Post post);
}
