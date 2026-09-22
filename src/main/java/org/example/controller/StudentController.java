package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dtoobject.StudentDto;
import org.example.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/students")
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentDto> findAll() {
        log.info("Getting all students");
        return studentService.findAll();
    }

    @GetMapping("{id}")
    public StudentDto findById(@PathVariable Long id) {
        log.info("Getting student by id: {}", id);
        return studentService.findById(id);
    }

    @PostMapping
    public StudentDto create(@RequestBody StudentDto dto) {
        log.info("Creating student: {}", dto);
        return studentService.create(dto);
    }

    @PutMapping("{id}")
    public StudentDto update(@PathVariable Long id, @RequestBody StudentDto dto) {
        log.info("Updating student by id: {}", id);
        return studentService.update(id, dto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting student by id: {}", id);
        studentService.delete(id);
    }
}
