package com.project.byeoryback.domain.auth.service;


import com.project.byeoryback.domain.auth.dto.PostRequest;
import com.project.byeoryback.domain.user.entity.Post.*;
import com.project.byeoryback.domain.user.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 1. 전체 조회 (최신순)
    public List<Post> getAllPosts() {
        return postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    // 2. 게시글 생성
    @Transactional
    public Post createPost(PostRequest request) {
        Post post = Post.builder()
                .title(request.getTitle())
                .blocks(request.getBlocks())
                .stickers(request.getStickers())
                .floatingTexts(request.getFloatingTexts())
                .floatingImages(request.getFloatingImages())
                // .user(user) // 나중에 로그인 기능 붙으면 유저 정보 추가
                .build();

        return postRepository.save(post);
    }

    // 3. 게시글 수정
    @Transactional
    public Post updatePost(Long id, PostRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        // Dirty Checking을 이용한 업데이트 (Setter나 update 메서드 필요)
        // 여기서는 Builder 패턴으로 설명드렸으니, Entity에 update 메서드를 추가하는 것이 좋습니다.
        // 편의상 새 객체로 덮어쓰거나, 아래와 같이 로직을 짭니다.

        // *주의: Post Entity에 @Setter가 없거나 update 메서드가 없다면 추가해야 합니다.
        // 아래는 Entity에 update 메서드가 있다고 가정하거나, Builder로 새로 만드는 방식입니다.
        // 가장 깔끔한 건 Post Entity 안에 updateFromDTO 같은 메서드를 만드는 것입니다.

        // (임시) Entity에 @Setter가 있다면 이렇게 씁니다.
        // post.setTitle(request.getTitle());
        // post.setBlocks(request.getBlocks());
        // ...

        // 하지만 더 좋은 방법은 Post Entity에 아래 메서드를 추가하고 호출하는 것입니다.
        post.update(
                request.getTitle(),
                request.getBlocks(),
                request.getStickers(),
                request.getFloatingTexts(),
                request.getFloatingImages()
        );

        return post; // Transactional 어노테이션 덕분에 자동 저장됨
    }

    // 4. 삭제
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}