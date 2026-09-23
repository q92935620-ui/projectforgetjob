package org.example.controller;

import org.example.dtoobject.PostDto;
import org.example.entity.Post;
import org.example.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@DisplayName("PostController")
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private PostService postService;

    @Test
    void findAll() throws Exception {
        when(postService.findAll()).thenReturn(List.of());
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk()).andExpect(content().json("[]"));
        verify(postService).findAll();


    }

    @Test
    void findById() throws Exception{
        Long id = 1L;
        PostDto postDto = new PostDto();
        postDto.setId(id);
        postDto.setPlace("Moscow");
        when(postService.findById(anyLong())).thenReturn(postDto);
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.place").value("Moscow"));

    }

    @Test
    void create() throws Exception {
        PostDto postDto = new PostDto();
        postDto.setPlace("Moscow");
        postDto.setId(1L);
        when(postService.create(postDto)).thenReturn(postDto);
        mockMvc.perform(post("api/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.place").value("Moscow"));

    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}