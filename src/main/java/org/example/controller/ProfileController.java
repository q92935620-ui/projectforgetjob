package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dtoobject.ProfileDto;
import org.example.service.ProfileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("api/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public List<ProfileDto> findAll() {
        log.info("Getting all profiles");
        return profileService.findAll();
    }

    @GetMapping("{id}")
    public ProfileDto findById(@PathVariable Long id) {
        log.info("Getting profile by id: {}", id);
        return profileService.findById(id);
    }

    @PostMapping
    public ProfileDto create(@RequestBody ProfileDto dto) {
        log.info("Creating profile: {}", dto);
        return profileService.create(dto);
    }

    @PutMapping("{id}")
    public ProfileDto update(@PathVariable Long id, @RequestBody ProfileDto dto) {
        log.info("Updating profile by id: {}", id);
        return profileService.update(id, dto);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        log.info("Deleting profile by id: {}", id);
        profileService.delete(id);
    }
}
