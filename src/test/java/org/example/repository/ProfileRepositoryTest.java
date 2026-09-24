package org.example.repository;

import org.example.entity.Profile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("ProfileRepository")
class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("сохраняет и находит профиль по ID")
    void shouldSaveAndFindById() {
        Profile profile = new Profile();
        profile.setFirstName("Иван");
        profile.setLastName("Петров");
        profile.setBalance(1000L);
        profile.setStatus("active");

        Profile saved = profileRepository.save(profile);
        assertNotNull(saved.getId());

        Profile found = profileRepository.findById(saved.getId()).orElse(null);
        assertNotNull(found);
        assertEquals("Иван", found.getFirstName());
        assertEquals("Петров", found.getLastName());
        assertEquals(1000L, found.getBalance());
        assertEquals("active", found.getStatus());
    }

    @Test
    @DisplayName("находит профиль по имени и фамилии через кастомный запрос")
    void shouldFindProfileByFirstNameAndLastName() {
        Profile profile = new Profile();
        profile.setFirstName("Анна");
        profile.setLastName("Сидорова");
        profile.setBalance(500L);
        profile.setStatus("active");

        profileRepository.save(profile);

        Optional<Profile> found = profileRepository.findBalanceByFirstNameAndLastName("Анна", "Сидорова");

        assertTrue(found.isPresent());
        assertEquals("Анна", found.get().getFirstName());
        assertEquals("Сидорова", found.get().getLastName());
    }

    @Test
    @DisplayName("успешно удаляет профиль")
    void shouldDeleteProfile() {
        Profile profile = new Profile();
        profile.setFirstName("Тест");
        profile.setLastName("Тест");
        profile.setBalance(0L);

        Profile saved = profileRepository.save(profile);
        Long id = saved.getId();

        profileRepository.deleteById(id);

        assertFalse(profileRepository.existsById(id));
    }

    @Test
    @DisplayName("находит все профили")
    void shouldFindAll() {
        Profile profile = new Profile();
        profile.setFirstName("Тест");
        profile.setLastName("Тест");
        profileRepository.save(profile);

        assertFalse(profileRepository.findAll().isEmpty());
    }
}
