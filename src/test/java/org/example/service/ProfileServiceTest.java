package org.example.service;

import org.example.dtoobject.ProfileDto;
import org.example.dtoobject.mapping.ProfileMapping;
import org.example.entity.Profile;
import org.example.repository.ProfileRepository;
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

@DisplayName("ProfileService")
@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    ProfileRepository profileRepository;

    @Mock
    ProfileMapping profileMapping;

    @InjectMocks
    ProfileService profileService;

    @Nested
    @DisplayName("findAll()")
    class FindAll {
        @Test
        @DisplayName("возвращает пустой список, если профилей нет")
        void shouldReturnEmptyList() {
            when(profileRepository.findAll()).thenReturn(List.of());

            List<ProfileDto> result = profileService.findAll();

            assertTrue(result.isEmpty());
            verifyNoInteractions(profileMapping);
            verify(profileRepository).findAll();
        }

        @Test
        @DisplayName("возвращает список DTO с маппингом")
        void shouldReturnMappedDtoList() {
            Profile profile = new Profile();
            profile.setId(1L);
            profile.setFirstName("Иван");
            profile.setLastName("Петров");

            ProfileDto dto = new ProfileDto();
            dto.setId(1L);
            dto.setFirstName("Иван");
            dto.setLastName("Петров");

            when(profileRepository.findAll()).thenReturn(List.of(profile));
            when(profileMapping.toDto(profile)).thenReturn(dto);

            List<ProfileDto> result = profileService.findAll();

            assertEquals(1, result.size());
            assertEquals(dto, result.get(0));
            verify(profileMapping).toDto(profile);
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {
        @Test
        @DisplayName("возвращает DTO, если профиль найден")
        void shouldReturnDto_whenExists() {
            Long id = 1L;
            Profile profile = new Profile();
            profile.setId(id);
            profile.setFirstName("Анна");

            ProfileDto dto = new ProfileDto();
            dto.setId(id);
            dto.setFirstName("Анна");

            when(profileRepository.findById(id)).thenReturn(Optional.of(profile));
            when(profileMapping.toDto(profile)).thenReturn(dto);

            ProfileDto result = profileService.findById(id);

            assertNotNull(result);
            assertEquals(id, result.getId());
            verify(profileRepository).findById(id);
            verify(profileMapping).toDto(profile);
        }

        @Test
        @DisplayName("кидает исключение, если профиль не найден")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            when(profileRepository.findById(id)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> profileService.findById(id));

            assertTrue(ex.getMessage().contains("99"));
            verify(profileRepository).findById(id);
            verifyNoInteractions(profileMapping);
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {
        @Test
        @DisplayName("создает и возвращает DTO")
        void shouldCreateAndReturnDto() {
            ProfileDto dto = new ProfileDto();
            dto.setFirstName("Иван");
            dto.setLastName("Сидоров");

            Profile profile = new Profile();
            profile.setId(1L);
            profile.setFirstName("Иван");
            profile.setLastName("Сидоров");

            ProfileDto savedDto = new ProfileDto();
            savedDto.setId(1L);
            savedDto.setFirstName("Иван");
            savedDto.setLastName("Сидоров");

            when(profileMapping.toEntity(dto)).thenReturn(profile);
            when(profileRepository.save(profile)).thenReturn(profile);
            when(profileMapping.toDto(profile)).thenReturn(savedDto);

            ProfileDto result = profileService.create(dto);

            assertNotNull(result);
            assertEquals(savedDto, result);
            verify(profileMapping).toEntity(dto);
            verify(profileRepository).save(profile);
            verify(profileMapping).toDto(profile);
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {
        @Test
        @DisplayName("обновляет профиль и возвращает DTO")
        void shouldUpdateAndReturnDto() {
            Long id = 1L;
            Profile existingProfile = new Profile();
            existingProfile.setId(id);
            existingProfile.setFirstName("Старое");
            existingProfile.setLastName("Фамилия");
            existingProfile.setBalance(100L);

            ProfileDto updateDto = new ProfileDto();
            updateDto.setFirstName("Новое");
            updateDto.setLastName("НоваяФамилия");
            updateDto.setBalance(500L);
            updateDto.setStatus("active");

            Profile updatedProfile = new Profile();
            updatedProfile.setId(id);
            updatedProfile.setFirstName("Новое");
            updatedProfile.setLastName("НоваяФамилия");
            updatedProfile.setBalance(500L);
            updatedProfile.setStatus("active");

            ProfileDto resultDto = new ProfileDto();
            resultDto.setId(id);
            resultDto.setFirstName("Новое");
            resultDto.setLastName("НоваяФамилия");
            resultDto.setBalance(500L);
            resultDto.setStatus("active");

            when(profileRepository.findById(id)).thenReturn(Optional.of(existingProfile));
            when(profileRepository.save(existingProfile)).thenReturn(updatedProfile);
            when(profileMapping.toDto(updatedProfile)).thenReturn(resultDto);

            ProfileDto result = profileService.update(id, updateDto);

            assertNotNull(result);
            assertEquals("Новое", result.getFirstName());
            assertEquals(500L, result.getBalance());
            verify(profileRepository).findById(id);
            verify(profileRepository).save(existingProfile);
        }

        @Test
        @DisplayName("кидает исключение при обновлении несуществующего профиля")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            ProfileDto dto = new ProfileDto();
            when(profileRepository.findById(id)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> profileService.update(id, dto));

            assertTrue(ex.getMessage().contains("99"));
            verify(profileRepository).findById(id);
            verify(profileRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {
        @Test
        @DisplayName("успешно удаляет профиль")
        void shouldDeleteSuccessfully() {
            Long id = 1L;
            when(profileRepository.existsById(id)).thenReturn(true);

            profileService.delete(id);

            verify(profileRepository).existsById(id);
            verify(profileRepository).deleteById(id);
        }

        @Test
        @DisplayName("кидает исключение при удалении несуществующего профиля")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            when(profileRepository.existsById(id)).thenReturn(false);

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> profileService.delete(id));

            assertTrue(ex.getMessage().contains("99"));
            verify(profileRepository).existsById(id);
            verify(profileRepository, never()).deleteById(any());
        }
    }
}
