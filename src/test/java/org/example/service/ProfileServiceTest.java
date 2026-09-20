package org.example.service;

import org.example.dtoobject.mapping.ProfileMapping;
import org.example.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {
    @Spy
    ProfileMapping profileMapping;

    @Mock
    ProfileRepository profileRepository;

    @InjectMocks
    ProfileService profileService;

    @Test
    void findAll() {
    }

    @Test
    void create() {
    }



    @Test
    void delete() {
    }
}