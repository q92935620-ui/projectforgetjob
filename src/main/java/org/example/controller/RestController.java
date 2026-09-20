package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dtoobject.ProfileDto;
import org.example.dtoobject.StudentDto;
import org.example.service.ProfileService;
import org.example.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@org.springframework.web.bind.annotation.RestController
@RequestMapping("user")


public class RestController {
    private final StudentService studentService;
    private final ProfileService profileService;
    @GetMapping
    public List<StudentDto> named_controller_its_doest_matter(){
        log.info("somthing going ");
        return studentService.findAll() ;
    }


}
