package org.example.dtoobject.mapping;

import org.example.dtoobject.ProfileDto;
import org.example.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfileMapping {
    ProfileDto toDto(Profile profile);
    @Mapping(target = "id",ignore = true)
    Profile toEntity(ProfileDto profileDto);
}