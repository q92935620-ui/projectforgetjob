package org.example.dtoobject;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Profile;
@NoArgsConstructor
@Data
public class StudentDto {
    Long id;
    String firstName;
    String lastName;
    Profile profile;
}
