package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Comment;
import com.example.onlybunsbe.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByPost(Post post);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post = :post AND c.createdAt > :afterDate")
    long countCommentsAfterDate(Post post, Instant afterDate);


    @Query("SELECT COUNT(c) FROM Comment c WHERE c.createdAt >= :date")
    long countCommentsAfterDate(@Param("date") Instant date);
}
