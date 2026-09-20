package org.example.service;

import org.example.dtoobject.PostDto;
import org.example.dtoobject.mapping.PostMapping;
import org.example.entity.Post;
import org.example.repository.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    PostRepository postRepository;

    @Mock
    PostMapping postMapping;


    @InjectMocks
    PostService postService;


    @Test
    void findById_shouldReturnDto_whenPostExists() {
        Long id = 1L;

        Post post = new Post();
        post.setId(id);
        post.setTime("12:00");
        post.setPlace("Москва");

        PostDto expectedDto = new PostDto();
        expectedDto.setId(id);
        expectedDto.setTime("12:00");
        expectedDto.setPlace("Москва");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postMapping.toDto(post)).thenReturn(expectedDto);

        PostDto trying = postService.findById(1L);

        assertNotNull(trying);
        assertEquals(expectedDto,trying);
        verify(postRepository).findById(id);
        verify(postMapping).toDto(post);


    }
    @Test
    void findById_shouldThrow_whenPostNotFound() {
        // ---------- Given ----------
        Long id = 99L;
        when(postRepository.findById(id)).thenReturn(Optional.empty());

        // ---------- When + Then ----------
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> postService.findById(id)
        );

        assertTrue(ex.getMessage().contains("99"));

        verify(postRepository).findById(id);
        // маппер НЕ должен вызываться, если поста нет
        verifyNoInteractions(postMapping);
    }
}