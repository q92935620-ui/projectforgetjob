package org.example.service;

import org.example.dtoobject.PostDto;
import org.example.dtoobject.mapping.PostMapping;
import org.example.entity.Post;
import org.example.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("PostService")
@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    PostRepository postRepository;

    @Mock
    PostMapping postMapping;

    @InjectMocks
    PostService postService;

    @Nested
    @DisplayName("findAll()")
    class FindAll {
        @Test
        @DisplayName("возвращает пустой список, если постов нет")
        void shouldReturnEmptyList() {
            when(postRepository.findAll()).thenReturn(List.of());

            List<PostDto> result = postService.findAll();

            assertTrue(result.isEmpty());
            verifyNoInteractions(postMapping);
            verify(postRepository).findAll();
        }

        @Test
        @DisplayName("возвращает список DTO с маппингом")
        void shouldReturnMappedDtoList() {
            Post post = new Post();
            post.setId(1L);
            post.setTime("12:00");
            post.setPlace("Москва");

            PostDto dto = new PostDto();
            dto.setId(1L);
            dto.setTime("12:00");
            dto.setPlace("Москва");

            when(postRepository.findAll()).thenReturn(List.of(post));
            when(postMapping.toDto(post)).thenReturn(dto);

            List<PostDto> result = postService.findAll();

            assertEquals(1, result.size());
            assertEquals(dto, result.get(0));
            verify(postMapping).toDto(post);
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {
        @ParameterizedTest
        @CsvSource({"1", "2", "3", "100", "999"})
        @DisplayName("возвращает DTO, если пост найден")
        void shouldReturnDto_whenExists(Long id) {
            Post post = new Post();
            post.setId(id);
            post.setTime("12:00");
            post.setPlace("Москва");

            PostDto dto = new PostDto();
            dto.setId(id);
            dto.setTime("12:00");
            dto.setPlace("Москва");

            when(postRepository.findById(id)).thenReturn(Optional.of(post));
            when(postMapping.toDto(post)).thenReturn(dto);

            PostDto result = postService.findById(id);

            assertNotNull(result);
            assertEquals(id, result.getId());
            verify(postRepository).findById(id);
            verify(postMapping).toDto(post);
        }

        @Test
        @DisplayName("кидает исключение, если пост не найден")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            when(postRepository.findById(id)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> postService.findById(id));

            assertTrue(ex.getMessage().contains("99"));
            verify(postRepository).findById(id);
            verifyNoInteractions(postMapping);
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {
        @Test
        @DisplayName("создает и возвращает DTO")
        void shouldCreateAndReturnDto() {
            PostDto dto = new PostDto();
            dto.setTime("15:00");
            dto.setPlace("СПб");

            Post post = new Post();
            post.setId(1L);
            post.setTime("15:00");
            post.setPlace("СПб");

            PostDto savedDto = new PostDto();
            savedDto.setId(1L);
            savedDto.setTime("15:00");
            savedDto.setPlace("СПб");

            when(postMapping.toEntity(dto)).thenReturn(post);
            when(postRepository.save(post)).thenReturn(post);
            when(postMapping.toDto(post)).thenReturn(savedDto);

            PostDto result = postService.create(dto);

            assertNotNull(result);
            assertEquals(savedDto, result);
            verify(postMapping).toEntity(dto);
            verify(postRepository).save(post);
            verify(postMapping).toDto(post);
        }
    }

    @Nested
    @DisplayName("update()")
    class Update {
        @ParameterizedTest
        @CsvSource({"1", "2", "3"})
        @DisplayName("обновляет пост и возвращает DTO")
        void shouldUpdateAndReturnDto(Long id) {
            Post existingPost = new Post();
            existingPost.setId(id);
            existingPost.setTime("10:00");
            existingPost.setPlace("Москва");

            PostDto updateDto = new PostDto();
            updateDto.setTime("18:00");
            updateDto.setPlace("Казань");

            Post updatedPost = new Post();
            updatedPost.setId(id);
            updatedPost.setTime("18:00");
            updatedPost.setPlace("Казань");

            PostDto resultDto = new PostDto();
            resultDto.setId(id);
            resultDto.setTime("18:00");
            resultDto.setPlace("Казань");

            when(postRepository.findById(id)).thenReturn(Optional.of(existingPost));
            when(postRepository.save(existingPost)).thenReturn(updatedPost);
            when(postMapping.toDto(updatedPost)).thenReturn(resultDto);

            PostDto result = postService.update(id, updateDto);

            assertNotNull(result);
            assertEquals("18:00", result.getTime());
            assertEquals("Казань", result.getPlace());
            verify(postRepository).findById(id);
            verify(postRepository).save(existingPost);
        }

        @Test
        @DisplayName("кидает исключение при обновлении несуществующего поста")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            PostDto dto = new PostDto();
            when(postRepository.findById(id)).thenReturn(Optional.empty());

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> postService.update(id, dto));

            assertTrue(ex.getMessage().contains("99"));
            verify(postRepository).findById(id);
            verify(postRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("delete()")
    class Delete {
        @ParameterizedTest
        @CsvSource({"1", "2", "3", "100"})
        @DisplayName("успешно удаляет пост")
        void shouldDeleteSuccessfully(Long id) {
            when(postRepository.existsById(id)).thenReturn(true);

            postService.delete(id);

            verify(postRepository).existsById(id);
            verify(postRepository).deleteById(id);
        }

        @Test
        @DisplayName("кидает исключение при удалении несуществующего поста")
        void shouldThrow_whenNotFound() {
            Long id = 99L;
            when(postRepository.existsById(id)).thenReturn(false);

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> postService.delete(id));

            assertTrue(ex.getMessage().contains("99"));
            verify(postRepository).existsById(id);
            verify(postRepository, never()).deleteById(any());
        }
    }
}
