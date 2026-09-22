package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dtoobject.ProfileDto;
import org.example.dtoobject.mapping.ProfileMapping;
import org.example.entity.Profile;
import org.example.repository.ProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final ProfileMapping profileMapping;

    @Transactional(readOnly = true)
    public List<ProfileDto> findAll() {
        return profileRepository.findAll().stream().map(profileMapping::toDto).toList();
    }

    @Transactional(readOnly = true)
    public ProfileDto findById(Long id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
        return profileMapping.toDto(profile);
    }

    @Transactional
    public ProfileDto create(ProfileDto dto) {
        Profile profile = profileMapping.toEntity(dto);
        Profile saved = profileRepository.save(profile);
        return profileMapping.toDto(saved);
    }

    @Transactional
    public ProfileDto update(Long id, ProfileDto dto) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile not found with id: " + id));
        profile.setBalance(dto.getBalance());
        profile.setStatus(dto.getStatus());
        profile.setFirstName(dto.getFirstName());
        profile.setLastName(dto.getLastName());
        return profileMapping.toDto(profileRepository.save(profile));
    }

    @Transactional
    public void delete(Long id) {
        if (!profileRepository.existsById(id)) {
            throw new RuntimeException("Profile not found with id: " + id);
        }
        profileRepository.deleteById(id);
    }
}