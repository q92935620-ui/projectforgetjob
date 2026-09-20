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
    public PostDto findById(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Runtime exeption" + id) );
        return postMapping.toDto(post);
    }

}
