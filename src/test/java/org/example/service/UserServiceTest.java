package org.example.service;

import org.example.dtoobject.UserDto;
import org.example.dtoobject.mapping.UserMapping;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("UserService")
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapping userMapping;

    @InjectMocks
    UserService userService;

    @Nested
    @DisplayName("findAll()")
    class FindAll {
        @Test
        @DisplayName("возвращает пустой список, если пользователей нет")
        void shouldReturnEmptyList() {
            when(userRepository.findAll()).thenReturn(List.of());

            List<UserDto> result = userService.findAll();

            assertTrue(result.isEmpty());
            verifyNoInteractions(userMapping);
            verify(userRepository).findAll();
        }

        @Test
        @DisplayName("возвращает список DTO с маппингом")
        void shouldReturnMappedDtoList() {
            User user = new User();
            user.setId(1L);
            user.setName("Иван");
            user.setTeg("@ivan");

            UserDto dto = new UserDto();
            dto.setId(1L);
            dto.setName("Иван");
            dto.setTeg("@ivan");

            when(userRepository.findAll()).thenReturn(List.of(user));
            when(userMapping.toDto(user)).thenReturn(dto);

            List<UserDto> result = userService.findAll();

            assertEquals(1, result.size());
            assertEquals(dto, result.get(0));
            verify(userMapping).toDto(user);
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {
        @Test
        @DisplayName("возвращает DTO, если пользователь найден")
        void shouldReturnDto_whenExists() {
            Long id = 1L;
            User user = new User();
            user.setId(id);
            user.setName("Анна");

            UserDto dto = new UserDto();
            dto.setId(id);
            dto.setName("Анна");

            when(userRepository.findById(id)).thenReturn(Optional.of(user));
            when(userMapping.toDto(user)).thenReturn(dto);

            UserDto result = userService.findById(id);

            assertNotNull(result);
            assertEquals(id, result.getId());
            verify(userRepository).findById(id);
            verify(userMapping).toDto(user);
        }

        @Test
        @DisplayName("кидает исключение, если пользователь не найден")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            when(userRepository.findById(id)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> userService.findById(id));

            assertTrue(ex.getMessage().contains("99"));
            verify(userRepository).findById(id);
            verifyNoInteractions(userMapping);
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {
        @Test
        @DisplayName("создает и возвращает DTO")
        void shouldCreateAndReturnDto() {
            UserDto dto = new UserDto();
            dto.setName("Иван");
            dto.setTeg("@ivan");

            User user = new User();
            user.setId(1L);
            user.setName("Иван");
            user.setTeg("@ivan");

            UserDto savedDto = new UserDto();
            savedDto.setId(1L);
            savedDto.setName("Иван");
            savedDto.setTeg("@ivan");

            when(userMapping.toEntity(dto)).thenReturn(user);
            when(userRepository.save(user)).thenReturn(user);
            when(userMapping.toDto(user)).thenReturn(savedDto);

            UserDto result = userService.create(dto);

            assertNotNull(result);
            assertEquals(savedDto, result);
            verify(userMapping).toEntity(dto);
            verify(userRepository).save(user);
            verify(userMapping).toDto(user);
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {
        @Test
        @DisplayName("обновляет пользователя и возвращает DTO")
        void shouldUpdateAndReturnDto() {
            Long id = 1L;
            User existingUser = new User();
            existingUser.setId(id);
            existingUser.setName("СтароеИмя");
            existingUser.setTeg("@old");
            existingUser.setNumber("123");

            UserDto updateDto = new UserDto();
            updateDto.setName("НовоеИмя");
            updateDto.setTeg("@new");
            updateDto.setNumber("456");

            User updatedUser = new User();
            updatedUser.setId(id);
            updatedUser.setName("НовоеИмя");
            updatedUser.setTeg("@new");
            updatedUser.setNumber("456");

            UserDto resultDto = new UserDto();
            resultDto.setId(id);
            resultDto.setName("НовоеИмя");
            resultDto.setTeg("@new");
            resultDto.setNumber("456");

            when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
            when(userRepository.save(existingUser)).thenReturn(updatedUser);
            when(userMapping.toDto(updatedUser)).thenReturn(resultDto);

            UserDto result = userService.update(id, updateDto);

            assertNotNull(result);
            assertEquals("НовоеИмя", result.getName());
            assertEquals("@new", result.getTeg());
            assertEquals("456", result.getNumber());
            verify(userRepository).findById(id);
            verify(userRepository).save(existingUser);
        }

        @Test
        @DisplayName("кидает исключение при обновлении несуществующего пользователя")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            UserDto dto = new UserDto();
            when(userRepository.findById(id)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> userService.update(id, dto));

            assertTrue(ex.getMessage().contains("99"));
            verify(userRepository).findById(id);
            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {
        @Test
        @DisplayName("успешно удаляет пользователя")
        void shouldDeleteSuccessfully() {
            Long id = 1L;
            when(userRepository.existsById(id)).thenReturn(true);

            userService.delete(id);

            verify(userRepository).existsById(id);
            verify(userRepository).deleteById(id);
        }

        @Test
        @DisplayName("кидает исключение при удалении несуществующего пользователя")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            when(userRepository.existsById(id)).thenReturn(false);

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> userService.delete(id));

            assertTrue(ex.getMessage().contains("99"));
            verify(userRepository).existsById(id);
            verify(userRepository, never()).deleteById(any());
        }
    }
}
