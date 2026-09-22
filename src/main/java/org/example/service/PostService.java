package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dtoobject.PostDto;
import org.example.dtoobject.mapping.PostMapping;
import org.example.entity.Post;
import org.example.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final PostMapping postMapping;

    @Transactional(readOnly = true)
    public List<PostDto> findAll() {
        return postRepository.findAll().stream()
                .map(postMapping::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public PostDto findById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        return postMapping.toDto(post);
    }

    @Transactional
    public PostDto create(PostDto dto) {
        Post post = postMapping.toEntity(dto);
        Post saved = postRepository.save(post);
        return postMapping.toDto(saved);
    }

    @Transactional
    public PostDto update(Long id, PostDto dto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + id));
        post.setTime(dto.getTime());
        post.setPlace(dto.getPlace());
        return postMapping.toDto(postRepository.save(post));
    }

    @Transactional
    public void delete(Long id) {
        if (!postRepository.existsById(id)) {
            throw new RuntimeException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }
}
