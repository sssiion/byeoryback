package com.project.byeoryback.domain.community.repository;

import com.project.byeoryback.domain.community.entity.Community;
import com.project.byeoryback.domain.community.entity.CommunityLike;
import com.project.byeoryback.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityLikeRepository extends JpaRepository<CommunityLike, Long> {
    boolean existsByUserAndCommunity(User user, Community community);

    void deleteByUserAndCommunity(User user, Community community);
}
