package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc(); // Dodaje sortiranje postova po datumu kreiranja
    @Query("SELECT COUNT(p) FROM Post p WHERE p.createdAt >= :date")
    long countPostsAfterDate(@Param("date") Instant date);
}


