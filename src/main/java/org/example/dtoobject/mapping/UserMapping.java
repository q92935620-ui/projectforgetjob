package org.example.dtoobject.mapping;

import org.example.dtoobject.UserDto;
import org.example.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapping {

    UserDto toDto(User user);

    @Mapping(target = "id",ignore = true)
    User toEntity(UserDto userDto);
}
