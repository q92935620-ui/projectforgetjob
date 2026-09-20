package org.example.dtoobject.mapping;

import org.example.dtoobject.PostDto;
import org.example.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapping {

    PostDto toDto(Post post);

    @Mapping(target = "id",ignore = true)
    Post toEntity(PostDto postDto);


}
