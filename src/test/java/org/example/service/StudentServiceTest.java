package org.example.service;

import org.example.dtoobject.StudentDto;
import org.example.dtoobject.mapping.StudentMapping;
import org.example.entity.Profile;
import org.example.entity.Student;
import org.example.repository.StudentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("StudentService")
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    StudentRepository studentRepository;

    @Mock
    StudentMapping studentMapping;

    @InjectMocks
    StudentService studentService;

    @Nested
    @DisplayName("findAll()")
    class FindAll {
        @Test
        @DisplayName("возвращает пустой список, если студентов нет")
        void shouldReturnEmptyList() {
            when(studentRepository.findAll()).thenReturn(List.of());

            List<StudentDto> result = studentService.findAll();

            assertTrue(result.isEmpty());
            verifyNoInteractions(studentMapping);
            verify(studentRepository).findAll();
        }

        @Test
        @DisplayName("возвращает список DTO с маппингом")
        void shouldReturnMappedDtoList() {
            Student student = new Student();
            student.setId(1L);
            student.setFirstName("Иван");
            student.setLastName("Петров");

            StudentDto dto = new StudentDto();
            dto.setId(1L);
            dto.setFirstName("Иван");
            dto.setLastName("Петров");

            when(studentRepository.findAll()).thenReturn(List.of(student));
            when(studentMapping.toDto(student)).thenReturn(dto);

            List<StudentDto> result = studentService.findAll();

            assertEquals(1, result.size());
            assertEquals(dto, result.get(0));
            verify(studentMapping).toDto(student);
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {
        @ParameterizedTest
        @CsvSource({"1", "2", "3", "100", "999"})
        @DisplayName("возвращает DTO, если студент найден")
        void shouldReturnDto_whenExists(Long id) {
            Student student = new Student();
            student.setId(id);
            student.setFirstName("Анна");

            StudentDto dto = new StudentDto();
            dto.setId(id);
            dto.setFirstName("Анна");

            when(studentRepository.findById(id)).thenReturn(Optional.of(student));
            when(studentMapping.toDto(student)).thenReturn(dto);

            StudentDto result = studentService.findById(id);

            assertNotNull(result);
            assertEquals(id, result.getId());
            verify(studentRepository).findById(id);
            verify(studentMapping).toDto(student);
        }

        @Test
        @DisplayName("кидает исключение, если студент не найден")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            when(studentRepository.findById(id)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> studentService.findById(id));

            assertTrue(ex.getMessage().contains("99"));
            verify(studentRepository).findById(id);
            verifyNoInteractions(studentMapping);
        }
    }

    @Nested
    @DisplayName("findStudentById()")
    class FindStudentById {
        @ParameterizedTest
        @CsvSource({"2","1","3"})

        @DisplayName("возвращает DTO, если студент найден")
        void shouldReturnDto_whenExists(Long id) {

            Student student = new Student();
            student.setId(id);
            student.setFirstName("Иван");

            StudentDto dto = new StudentDto();
            dto.setId(id);
            dto.setFirstName("Иван");

            when(studentRepository.findStudentById(id)).thenReturn(Optional.of(student));
            when(studentMapping.toDto(student)).thenReturn(dto);

            StudentDto result = studentService.findStudentById(id);

            assertNotNull(result);
            assertEquals(dto, result);
            verify(studentRepository).findStudentById(id);
            verify(studentMapping).toDto(student);
        }

        @Test
        @DisplayName("кидает исключение, если студент не найден")
        void shouldThrow_whenNotFound() {
            Long id = 12L;
            when(studentRepository.findStudentById(id)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> studentService.findStudentById(id));

            assertTrue(ex.getMessage().contains("StudentNotFFOund"));
            verify(studentRepository).findStudentById(id);
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {
        @Test
        @DisplayName("создает и возвращает DTO")
        void shouldCreateAndReturnDto() {
            StudentDto dto = new StudentDto();
            dto.setFirstName("Иван");
            dto.setLastName("Сидоров");

            Student student = new Student();
            student.setId(1L);
            student.setFirstName("Иван");
            student.setLastName("Сидоров");

            StudentDto savedDto = new StudentDto();
            savedDto.setId(1L);
            savedDto.setFirstName("Иван");
            savedDto.setLastName("Сидоров");

            when(studentMapping.toEntity(dto)).thenReturn(student);
            when(studentRepository.save(student)).thenReturn(student);
            when(studentMapping.toDto(student)).thenReturn(savedDto);

            StudentDto result = studentService.create(dto);

            assertNotNull(result);
            assertEquals(savedDto, result);
            verify(studentMapping).toEntity(dto);
            verify(studentRepository).save(student);
            verify(studentMapping).toDto(student);
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {
        @ParameterizedTest
        @CsvSource({"1", "2", "3"})
        @DisplayName("обновляет студента и возвращает DTO")
        void shouldUpdateAndReturnDto(Long id) {
            Student existingStudent = new Student();
            existingStudent.setId(id);
            existingStudent.setFirstName("Старое");
            existingStudent.setLastName("Фамилия");

            StudentDto updateDto = new StudentDto();
            updateDto.setFirstName("Новое");
            updateDto.setLastName("НоваяФамилия");

            Student updatedStudent = new Student();
            updatedStudent.setId(id);
            updatedStudent.setFirstName("Новое");
            updatedStudent.setLastName("НоваяФамилия");

            StudentDto resultDto = new StudentDto();
            resultDto.setId(id);
            resultDto.setFirstName("Новое");
            resultDto.setLastName("НоваяФамилия");

            when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
            when(studentRepository.save(existingStudent)).thenReturn(updatedStudent);
            when(studentMapping.toDto(updatedStudent)).thenReturn(resultDto);

            StudentDto result = studentService.update(id, updateDto);

            assertNotNull(result);
            assertEquals("Новое", result.getFirstName());
            verify(studentRepository).findById(id);
            verify(studentRepository).save(existingStudent);
        }

        @Test
        @DisplayName("кидает исключение при обновлении несуществующего студента")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            StudentDto dto = new StudentDto();
            when(studentRepository.findById(id)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> studentService.update(id, dto));

            assertTrue(ex.getMessage().contains("99"));
            verify(studentRepository).findById(id);
            verify(studentRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {
        @ParameterizedTest
        @CsvSource({"1", "2", "3", "100"})
        @DisplayName("успешно удаляет студента")
        void shouldDeleteSuccessfully(Long id) {
            when(studentRepository.existsById(id)).thenReturn(true);

            studentService.delete(id);

            verify(studentRepository).existsById(id);
            verify(studentRepository).deleteById(id);
        }

        @Test
        @DisplayName("кидает исключение при удалении несуществующего студента")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            when(studentRepository.existsById(id)).thenReturn(false);

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> studentService.delete(id));

            assertTrue(ex.getMessage().contains("99"));
            verify(studentRepository).existsById(id);
            verify(studentRepository, never()).deleteById(any());
        }
    }
}
