package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Post;
import com.example.onlybunsbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc(); // Dodaje sortiranje postova po datumu kreiranja
    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= :date")
    long countPostsAfterDate(@Param("date") Instant date);
    List<Post> findByUserIn(Set<User> users);

    List<Post> findByUserId(Long userId);

    @Query("SELECT p FROM Post p WHERE p.user.id IN :userIds ORDER BY p.createdAt DESC")
    List<Post> findByUserIdInOrderByCreatedAtDesc(@Param("userIds") List<Long> userIds);

}


