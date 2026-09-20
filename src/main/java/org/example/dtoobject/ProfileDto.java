package org.example.dtoobject;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class ProfileDto {
    private Long id;
    private Long balance;
    private String status;
    private String firstName;
    private String lastName;
}