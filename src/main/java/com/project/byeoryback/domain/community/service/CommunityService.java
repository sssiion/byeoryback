package com.project.byeoryback.domain.community.service;

import com.project.byeoryback.domain.community.dto.CommunityDto;
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
    private final com.project.byeoryback.domain.community.repository.CommunityLikeRepository communityLikeRepository;
    private final com.project.byeoryback.domain.user.repository.UserRepository userRepository;

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
        Community community = Community.builder()
                .post(post)
                .user(post.getUser())
                .likeCount(0L)
                .viewCount(0L)
                .build();
        communityRepository.save(community);
    }

    /**
     * 커뮤니티 글 상세 조회 (조회수 증가 포함)
     * + 로그인한 유저의 좋아요 여부 체크
     */
    @Transactional
    public CommunityDto.Response getCommunity(Long communityId, Long userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 커뮤니티 글이 없습니다. id=" + communityId));

        community.increaseViewCount(); // 조회수 증가

        boolean isLiked = false;
        if (userId != null && userId != 0L) {
            com.project.byeoryback.domain.user.entity.User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. id=" + userId));
            isLiked = communityLikeRepository.existsByUserAndCommunity(user, community);
        }

        return CommunityDto.Response.from(community, isLiked);
    }

    // 단순 조회용 (오버로딩)
    @Transactional
    public Community getCommunity(Long communityId) {
        return communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 커뮤니티 글이 없습니다. id=" + communityId));
    }

    /**
     * 커뮤니티 목록 조회 (페이징)
     */
    @Transactional(readOnly = true)
    public org.springframework.data.domain.Page<CommunityDto.Response> getCommunityList(
            org.springframework.data.domain.Pageable pageable, Long userId) {
        return communityRepository.findAll(pageable)
                .map(community -> {
                    boolean isLiked = false;
                    if (userId != null && userId != 0L) {
                        try {
                            com.project.byeoryback.domain.user.entity.User user = userRepository
                                    .getReferenceById(userId);
                            // getReferenceById는 프록시만 가져오므로 쿼리 절약 가능하지만, exists 쿼리시 어차피 ID 쓰니 상관없음.
                            // 안전하게 findById 없이 ID만으로 체크하려면 쿼리를 직접 짜야 함.
                            // 여기서는 간단하게 repository 메서드 활용.
                            // N+1 문제 발생 가능성 있음 (목록 10개 -> 좋아요 체크 10번).
                            // 최적화 필요 시 user_id, community_id in (...) 로 한 번에 조회해야 함.
                            // 일단 기능 구현 우선.
                            isLiked = communityLikeRepository.existsByUserAndCommunity(user, community);
                        } catch (Exception e) {
                            // 유저 없을 경우 무시
                        }
                    }
                    return CommunityDto.Response.from(community, isLiked);
                });
    }

    /**
     * 좋아요 토글
     */
    @Transactional
    public void toggleLike(Long communityId, Long userId) {
        Community community = communityRepository.findById(communityId)
                .orElseThrow(() -> new IllegalArgumentException("해당 커뮤니티 글이 없습니다. id=" + communityId));

        com.project.byeoryback.domain.user.entity.User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 존재하지 않습니다. id=" + userId));

        boolean exists = communityLikeRepository.existsByUserAndCommunity(user, community);

        if (exists) {
            // 이미 좋아요 누름 -> 취소
            communityLikeRepository.deleteByUserAndCommunity(user, community);
            community.decreaseLikeCount();
        } else {
            // 좋아요 안 누름 -> 추가
            com.project.byeoryback.domain.community.entity.CommunityLike like = com.project.byeoryback.domain.community.entity.CommunityLike
                    .builder()
                    .user(user)
                    .community(community)
                    .build();
            communityLikeRepository.save(like);
            community.increaseLikeCount();
        }
    }

    /**
     * 게시글이 삭제될 때 커뮤니티 데이터도 정리
     */
    @Transactional
    public void deleteByPostId(Long postId) {
        communityRepository.deleteByPostId(postId);
    }
}