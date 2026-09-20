package org.example.repository;

import org.example.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student,Long> {
@Query("SELECT s from Student s JOIN s.profile p WHERE p.status = :status")
List<Student> findAllByProfileStatus(@Param("status") String status);
@Query("SELECT s FROM Student s WHERE s.id = :id ")
Optional<Student> findStudentById(@Param("id") Long id );

}