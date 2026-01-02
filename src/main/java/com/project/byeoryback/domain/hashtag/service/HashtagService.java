package com.project.byeoryback.domain.hashtag.service;

import com.project.byeoryback.domain.hashtag.entity.Hashtag;
import com.project.byeoryback.domain.hashtag.entity.PostHashtag;
import com.project.byeoryback.domain.hashtag.repository.HashtagRepository;
import com.project.byeoryback.domain.hashtag.repository.PostHashtagRepository;
import com.project.byeoryback.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final PostHashtagRepository postHashtagRepository;

    @Transactional
    public void processHashtags(Post post, List<String> tagNames) {
        // Clear existing tags for this post
        postHashtagRepository.deleteAllByPostId(post.getId());

        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }

        // Remove duplicates from input
        Set<String> uniqueNames = tagNames.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        for (String name : uniqueNames) {
            Hashtag hashtag = hashtagRepository.findByName(name)
                    .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(name).build()));

            postHashtagRepository.save(PostHashtag.builder()
                    .post(post)
                    .hashtag(hashtag)
                    .build());
        }
    }

    public List<String> getHashtagsByPost(Long postId) {
        return postHashtagRepository.findAllByPostId(postId).stream()
                .map(ph -> ph.getHashtag().getName())
                .collect(Collectors.toList());
    }
}
