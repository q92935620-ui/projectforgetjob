package org.example.dtoobject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    Long id;
    String time;
    String place;
    User user;
}
