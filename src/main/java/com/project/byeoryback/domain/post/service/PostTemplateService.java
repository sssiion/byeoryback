package com.project.byeoryback.domain.post.service;

import com.project.byeoryback.domain.post.dto.PostTemplateDto;
import com.project.byeoryback.domain.post.entity.PostTemplate;
import com.project.byeoryback.domain.post.repository.PostTemplateRepository;
import com.project.byeoryback.domain.user.entity.User;
import com.project.byeoryback.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostTemplateService {

    private final PostTemplateRepository postTemplateRepository;
    private final UserRepository userRepository;

    public List<PostTemplateDto.Response> getMyTemplates(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return postTemplateRepository.findAllByUserOrderByCreatedAtDesc(user).stream()
                .map(PostTemplateDto.Response::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostTemplateDto.Response createTemplate(Long userId, PostTemplateDto.CreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PostTemplate template = PostTemplate.builder()
                .user(user)
                .name(request.getName())
                .styles(request.getStyles())
                .stickers(request.getStickers())
                .floatingImages(request.getFloatingImages())
                .defaultFontColor(request.getDefaultFontColor())
                .thumbnailUrl(request.getThumbnailUrl())
                .build();

        return PostTemplateDto.Response.from(postTemplateRepository.save(template));
    }

    @Transactional
    public void deleteTemplate(Long userId, Long templateId) {
        PostTemplate template = postTemplateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));

        if (!template.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("Not authorized to delete this template");
        }

        postTemplateRepository.delete(template);
    }

    public PostTemplateDto.Response getTemplate(Long templateId) {
        PostTemplate template = postTemplateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Template not found"));
        return PostTemplateDto.Response.from(template);
    }
}
