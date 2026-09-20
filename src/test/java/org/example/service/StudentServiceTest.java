package org.example.service;

import org.example.dtoobject.StudentDto;
import org.example.dtoobject.mapping.StudentMapping;
import org.example.entity.Profile;
import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    StudentRepository studentRepository;

    @Mock
    StudentMapping studentMapping;

    @InjectMocks
    StudentService studentService;


    @Test
    void findAllTest() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<StudentDto> student = studentService.findAll();
        assertTrue(student.isEmpty());


        verifyNoInteractions(studentMapping);
        verify(studentRepository).findAll();
    }



    @Test
    void findById() {
        Profile testprofile = new Profile();
        testprofile.setId(1L);
        Student teststudent = new Student();
        teststudent.setFirstName("testfname");
        teststudent.setLastName("testlname");
        teststudent.setProfile(testprofile);
        when(studentRepository.findById(anyLong())).thenAnswer(invocation -> {
            Long id = invocation.getArgument(0);

            teststudent.setId(id);
            return Optional.of(teststudent);
        });
        StudentDto expected = new StudentDto();
        expected.setId(23L);
        when(studentMapping.toDto(any(Student.class))).thenReturn(expected);

        StudentDto student = studentService.findById(23L);

       ArgumentCaptor<Student> captor = ArgumentCaptor.forClass(Student.class);
       verify(studentMapping).toDto(captor.capture());
       Student captore = captor.getValue();

        verify(studentMapping).toDto(any(Student.class));
        assertEquals(23L,student.getId());




    }
    @Test
    void findstudetByIdTest() {
        doThrow(new RuntimeException("not found")).when(studentRepository).findStudentById(anyLong());
        Long id  = 12L;

        assertThrows(RuntimeException.class,() -> studentService.findStudentById(id));
        verify(studentRepository).findStudentById(id);

    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {

    }

    @Test
    void assignProfile() {
    }
}