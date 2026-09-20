package org.example.dtoobject.mapping;

import org.example.dtoobject.StudentDto;
import org.example.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapping {


    StudentDto toDto(Student student);


    @Mapping(target = "id",ignore = true)
    Student toEntity(StudentDto studentDto);
}