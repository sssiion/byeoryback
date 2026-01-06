package com.project.byeoryback.domain.community.service;



import com.project.byeoryback.domain.community.entity.Community;
import com.project.byeoryback.domain.community.repository.CommunityRepository;
import com.project.byeoryback.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;

    /**
     * [핵심] PostService에서 호출: 게시글의 공개 여부(isPublic)에 따라 커뮤니티 글을 생성하거나 삭제
     */
    @Transactional
    public void syncPublicStatus(Post post) {
        boolean exists = communityRepository.findByPostId(post.getId()).isPresent();

        if (Boolean.TRUE.equals(post.getIsPublic())) {
            // 공개글인데 커뮤니티에 없으면 -> 생성
            if (!exists) {
                createCommunity(post);
            }
        } else {
            // 비공개글인데 커뮤니티에 있으면 -> 삭제 (숨김)
            if (exists) {
                communityRepository.deleteByPostId(post.getId());
            }
        }
    }

    private void createCommunity(Post post) {
        Community community = community.builder()
                .post(post)
                .user(post.getUser())
                .likeCount(0L)
                .viewCount(0L)
                .build();
        communityRepository.save(community);
    }

    /**
     * 커뮤니티 글 상세 조회 (조회수 증가 포함)
     */
    @Transactional
    public Community getCommunity(Long communityId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 커뮤니티 글이 없습니다. id=" + communityId));

        community.increaseViewCount(); // 조회수 증가
        return community;
    }

    /**
     * 게시글이 삭제될 때 커뮤니티 데이터도 정리
     */
    @Transactional
    public void deleteByPostId(Long postId) {
        communityRepository.deleteByPostId(postId);
    }
}