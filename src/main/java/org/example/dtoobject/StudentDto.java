package org.example.dtoobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.entity.Profile;
@NoArgsConstructor
@Data
@AllArgsConstructor
public class StudentDto {
    Long id;
    String firstName;
    String lastName;
    Profile profile;
}
