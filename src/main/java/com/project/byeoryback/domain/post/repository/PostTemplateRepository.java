package com.project.byeoryback.domain.post.repository;

import com.project.byeoryback.domain.post.entity.PostTemplate;
import com.project.byeoryback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTemplateRepository extends JpaRepository<PostTemplate, Long> {
    List<PostTemplate> findAllByUser(User user);

    List<PostTemplate> findAllByUserOrderByCreatedAtDesc(User user);
}
