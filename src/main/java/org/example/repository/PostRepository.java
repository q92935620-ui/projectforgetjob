package org.example.repository;

import org.example.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post,Long> {
    @Query("SELECT u FROM Post u WHERE u.place = :place")
    Optional<Post> findPostsWherePlace(@Param("place") String place);
    @EntityGraph(attributePaths = {"user"})
    List<Post> findAll();
    @Query("SELECT s FROM Post s JOIN FETCH s.user")
    List<Post> findAllByJPQL();
}
