package org.example;

import org.example.entity.Post;
import org.example.entity.Profile;
import org.example.entity.Student;
import org.example.entity.User;
import org.example.repository.PostRepository;
import org.example.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
public class Main implements CommandLineRunner {
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PostRepository postRepository;





    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        Student students = new Student();
        students.setFirstName("2prof");
        students.setLastName("profile2");

        Profile profiles = new Profile();
        profiles.setBalance(10023L);
        profiles.setStatus("drun");
        profiles.setFirstName("qwerty");
        profiles.setLastName("ostrov");
        students.setProfile(profiles);

        studentRepository.save(students);
        Post post1 = new Post();
        User user1 = new User();
        post1.setPlace("moskov");
        post1.setTime("11september");
        user1.setName("kiriluser1");
        user1.setNumber("+73333332222");
        user1.setTeg("@qweerty_drun");
        post1.setUser(user1);
        Post save1 = postRepository.save(post1);
        System.out.println(save1);


    }
}