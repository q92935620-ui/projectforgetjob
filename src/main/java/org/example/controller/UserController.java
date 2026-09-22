package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dtoobject.UserDto;
import org.example.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> findAll() {
        log.info("Getting all users");
        return userService.findAll();
    }

    @GetMapping("{id}")
    public UserDto findById(@PathVariable Long id) {
        log.info("Getting user by id: {}", id);
        return userService.findById(id);
    }

    @PostMapping
    public UserDto create(@RequestBody UserDto dto) {
        log.info("Creating user: {}", dto);
        return userService.create(dto);
    }

    @PutMapping("{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserDto dto) {
        log.info("Updating user by id: {}", id);
        return userService.update(id, dto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting user by id: {}", id);
        userService.delete(id);
    }
}
