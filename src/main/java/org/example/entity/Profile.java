package org.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "profiles")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "balance")
    private Long balance;

    @Column(name = "status")
    private String status;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;



    @OneToOne(mappedBy = "profile")
    private Student student;
}