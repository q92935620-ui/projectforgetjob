package org.example.repository;

import org.example.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ProfileRepository extends JpaRepository<Profile,Long> {
    @Query("SELECT s FROM Profile s WHERE s.firstName = :firstname AND s.lastName = :lastName")
    Optional<Profile> findBalanceByFirstNameAndLastName(@Param("firstname")String firstname, @Param("lastName") String lastName);
}
