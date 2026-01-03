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
        // 1. Clear existing tags using entity method (Cascade will handle deletion)
        post.clearPostHashtags();

        if (tagNames == null || tagNames.isEmpty()) {
            return;
        }

        // 2. Remove duplicates from input
        Set<String> uniqueNames = tagNames.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());

        // 3. Add new tags
        for (String name : uniqueNames) {
            Hashtag hashtag = hashtagRepository.findByName(name)
                    .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(name).build()));

            PostHashtag postHashtag = PostHashtag.builder()
                    .post(post)
                    .hashtag(hashtag)
                    .build();

            post.addPostHashtag(postHashtag);
        }
        // No explicit save needed for PostHashtag if Post is saved appropriately or
        // transaction commits
    }

    @Transactional
    public Hashtag findOrCreate(String name) {
        String trimmedName = name.trim();
        if (trimmedName.isEmpty()) {
            return null;
        }
        return hashtagRepository.findByName(trimmedName)
                .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(trimmedName).build()));
    }

    public List<String> getHashtagsByPost(Long postId) {
        return postHashtagRepository.findAllByPostId(postId).stream()
                .map(ph -> ph.getHashtag().getName())
                .collect(Collectors.toList());
    }
}
