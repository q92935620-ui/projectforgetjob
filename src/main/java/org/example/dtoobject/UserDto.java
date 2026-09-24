package org.example.dtoobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
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
