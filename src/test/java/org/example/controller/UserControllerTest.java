package org.example.controller;

import org.example.dtoobject.StudentDto;
import org.example.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
@DisplayName("StudentController")
class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentService studentService;

    @Test
    @DisplayName("GET /api/students возвращает пустой список")
    void findAll_shouldReturnEmptyList() throws Exception {
        when(studentService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(studentService).findAll();
    }

    @Test
    @DisplayName("GET /api/students/{id} возвращает DTO")
    void findById_shouldReturnDto() throws Exception {
        StudentDto dto = new StudentDto();
        dto.setId(1L);
        dto.setFirstName("Ivan");

        when(studentService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ivan"));

        verify(studentService).findById(1L);
    }

    @Test
    @DisplayName("POST /api/students создает студента")
    void create_shouldReturnCreated() throws Exception {
        StudentDto input = new StudentDto();
        input.setFirstName("Ivan");

        StudentDto created = new StudentDto();
        created.setId(1L);
        created.setFirstName("Ivan");

        when(studentService.create(any(StudentDto.class))).thenReturn(created);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ivan"));

        verify(studentService).create(any(StudentDto.class));
    }

    @Test
    @DisplayName("DELETE /api/students/{id} удаляет студента")
    void delete_shouldCallService() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk());

        verify(studentService).delete(1L);
    }
}