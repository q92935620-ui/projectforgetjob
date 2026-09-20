package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dtoobject.UserDto;
import org.example.dtoobject.mapping.UserMapping;
import org.example.entity.Student;
import org.example.entity.User;
import org.example.repository.PostRepository;
import org.example.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapping userMapping;


    @Transactional(readOnly = true)
    public UserDto findById(Long id ) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("id not found :" + id));
        return userMapping.toDto(user);
    }

    public UserDto create(UserDto userDto) {
        User user = userMapping.toEntity(userDto);
        User save = userRepository.save(user);
        return userMapping.toDto(save);
    }



}
