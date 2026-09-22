package org.example.config;

import org.example.entity.Post;
import org.example.entity.Profile;
import org.example.entity.Student;
import org.example.entity.User;
import org.example.repository.PostRepository;
import org.example.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

@Component
@org.springframework.context.annotation.Profile("dev")   // <-- работает только с -Dspring.profiles.active=dev
public class DevDataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;
    private final PostRepository postRepository;

    public DevDataInitializer(StudentRepository studentRepository,
                              PostRepository postRepository) {
        this.studentRepository = studentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public void run(String... args) {
        // твоя логика с save()
    }
}