package org.example.repository;

import org.example.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("UserRepository")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("сохраняет и находит пользователя по ID")
    void shouldSaveAndFindById() {
        User user = new User();
        user.setName("Иван");
        user.setTeg("@ivan");
        user.setNumber("+79991234567");

        User saved = userRepository.save(user);
        assertNotNull(saved.getId());

        User found = userRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Иван", found.getName());
        assertEquals("@ivan", found.getTeg());
        assertEquals("+79991234567", found.getNumber());
    }

    @Test
    @DisplayName("находит всех пользователей")
    void shouldFindAll() {
        User user = new User();
        user.setName("Тест");
        user.setTeg("@test");
        user.setNumber("+79990000000");
        userRepository.save(user);

        assertFalse(userRepository.findAll().isEmpty());
    }

    @Test
    @DisplayName("успешно удаляет пользователя")
    void shouldDeleteUser() {
        User user = new User();
        user.setName("Тест");
        user.setTeg("@test");
        user.setNumber("+79990000000");

        User saved = userRepository.save(user);
        Long id = saved.getId();

        userRepository.deleteById(id);

        assertFalse(userRepository.existsById(id));
    }
}
