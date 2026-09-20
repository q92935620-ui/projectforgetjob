package org.example.dtoobject;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserDto {
    Long id;

    String name;
    String teg;
    String number;
    public void userConstructor(String name,String teg){
        this.name=name;
        this.teg=teg;
    }
}
