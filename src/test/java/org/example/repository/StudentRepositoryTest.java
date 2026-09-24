package org.example.repository;

import org.example.entity.Profile;
import org.example.entity.Student;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("StudentRepository")
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("сохраняет и находит студента по ID")
    void shouldSaveAndFindById() {
        Profile profile = new Profile();
        profile.setFirstName("Иван");
        profile.setLastName("Петров");
        profile.setBalance(1000L);
        profileRepository.save(profile);

        Student student = new Student();
        student.setFirstName("Иван");
        student.setLastName("Петров");
        student.setProfile(profile);

        Student saved = studentRepository.save(student);
        assertNotNull(saved.getId());

        Student found = studentRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Иван", found.getFirstName());
        assertEquals("Петров", found.getLastName());
    }

    @Test
    @DisplayName("находит студента через кастомный запрос findStudentById")
    void shouldFindStudentById() {
        Profile profile = new Profile();
        profile.setFirstName("Анна");
        profile.setLastName("Сидорова");
        profileRepository.save(profile);

        Student student = new Student();
        student.setFirstName("Анна");
        student.setLastName("Сидорова");
        student.setProfile(profile);

        studentRepository.save(student);

        Optional<Student> found = studentRepository.findStudentById(student.getId());

        assertTrue(found.isPresent());
        assertEquals("Анна", found.get().getFirstName());
    }

    @Test

    @DisplayName("находит студентов по статусу профиля")
    void shouldFindAllByProfileStatus() {
        Profile profile = new Profile();

        profile.setFirstName("Тест");
        profile.setLastName("Тест");
        profile.setStatus("active");
        profileRepository.save(profile);

        Student student = new Student();

        student.setFirstName("Тест");
        student.setLastName("Тест");
        student.setProfile(profile);

        studentRepository.save(student);

        List<Student> students = studentRepository.findAllByProfileStatus("active");
        assertFalse(students.isEmpty());
        assertEquals(profile, students.get(0).getProfile());
    }

    @Test
    @DisplayName("успешно удаляет студента")
    void shouldDeleteStudent() {
        Profile profile = new Profile();
        profile.setFirstName("Тест");
        profile.setLastName("Тест");
        profileRepository.save(profile);

        Student student = new Student();
        student.setFirstName("Тест");
        student.setLastName("Тест");
        student.setProfile(profile);

        Student saved = studentRepository.save(student);
        Long id = saved.getId();

        studentRepository.deleteById(id);

        assertFalse(studentRepository.existsById(id));
    }
}
