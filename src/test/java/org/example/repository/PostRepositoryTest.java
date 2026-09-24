package org.example.repository;

import org.example.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("PostRepository")
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("сохраняет и находит пост по ID")
    void shouldSaveAndFindById() {
        Post post = new Post();
        post.setTime("12:00");
        post.setPlace("Москва");

        Post saved = postRepository.save(post);
        assertNotNull(saved.getId());

        Post found = postRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("12:00", found.getTime());
        assertEquals("Москва", found.getPlace());
    }

    @Test
    @DisplayName("находит посты по месту через кастомный запрос")
    void shouldFindPostsByPlace() {
        Post post1 = new Post();
        post1.setTime("10:00");
        post1.setPlace("СПб");

        Post post2 = new Post();
        post2.setTime("15:00");
        post2.setPlace("СПб");

        postRepository.save(post1);
        postRepository.save(post2);

        List<Post> posts = postRepository.findPostsWherePlace("СПб");

        assertFalse(posts.isEmpty());
        assertEquals(2, posts.size());
    }

    @Test
    @DisplayName("успешно удаляет пост")
    void shouldDeletePost() {
        Post post = new Post();
        post.setTime("18:00");
        post.setPlace("Казань");

        Post saved = postRepository.save(post);
        Long id = saved.getId();

        postRepository.deleteById(id);

        assertFalse(postRepository.existsById(id));
    }

    @Test
    @DisplayName("находит все посты")
    void shouldFindAll() {
        Post post = new Post();
        post.setTime("09:00");
        post.setPlace("Москва");
        postRepository.save(post);

        List<Post> posts = postRepository.findAll();
        assertFalse(posts.isEmpty());
    }
}
