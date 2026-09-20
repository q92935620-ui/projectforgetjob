package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dtoobject.StudentDto;
import org.example.dtoobject.mapping.StudentMapping;
import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapping studentMapping;

    @Transactional(readOnly = true)
    public List<StudentDto> findAll() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapping::toDto)
                .toList();
    }
    @Transactional(readOnly = true)
    public StudentDto findStudentById(Long id ) {
        Student student = studentRepository.
                findStudentById(id).orElseThrow(() -> new RuntimeException("StudentNotFFOund" + id));
        return studentMapping.toDto(student);
    }

    @Transactional(readOnly = true)
    public StudentDto findById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        return studentMapping.toDto(student);
    }

    @Transactional
    public StudentDto create(StudentDto dto) {
        Student student = studentMapping.toEntity(dto);
        log.info("crate student {}", dto);
        Student saved = studentRepository.save(student);

        return studentMapping.toDto(saved);
    }

    @Transactional
    public StudentDto update(Long id, StudentDto dto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        return studentMapping.toDto(studentRepository.save(student));
    }

    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Transactional
    public StudentDto assignProfile(Long studentId, Long profileId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        // profileRepository.findById... (если нужно связать)
        // Здесь нужен ProfileRepository — добавь его через конструктор при необходимости
        return studentMapping.toDto(studentRepository.save(student));
    }
}