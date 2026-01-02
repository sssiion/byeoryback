package com.project.byeoryback.domain.hashtag.repository;

import com.project.byeoryback.domain.hashtag.entity.PostHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostHashtagRepository extends JpaRepository<PostHashtag, Long> {
    List<PostHashtag> findAllByPostId(Long postId);

    List<PostHashtag> findAllByHashtagId(Long hashtagId);

    void deleteAllByPostId(Long postId);
}
