package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dtoobject.PostDto;
import org.example.service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public List<PostDto> findAll() {
        log.info("Getting all posts");
        return postService.findAll();
    }

    @GetMapping("{id}")
    public PostDto findById(@PathVariable Long id) {
        log.info("Getting post by id: {}", id);
        return postService.findById(id);
    }

    @PostMapping
    public PostDto create(@RequestBody PostDto dto) {
        log.info("Creating post: {}", dto);
        return postService.create(dto);
    }

    @PutMapping("{id}")
    public PostDto update(@PathVariable Long id, @RequestBody PostDto dto) {
        log.info("Updating post by id: {}", id);
        return postService.update(id, dto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting post by id: {}", id);
        postService.delete(id);
    }
}
