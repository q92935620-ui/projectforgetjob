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

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapping userMapping;

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(userMapping::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return userMapping.toDto(user);
    }

    @Transactional
    public UserDto create(UserDto userDto) {
        User user = userMapping.toEntity(userDto);
        User saved = userRepository.save(user);
        return userMapping.toDto(saved);
    }

    @Transactional
    public UserDto update(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setName(dto.getName());
        user.setTeg(dto.getTeg());
        user.setNumber(dto.getNumber());
        return userMapping.toDto(userRepository.save(user));
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }
}
