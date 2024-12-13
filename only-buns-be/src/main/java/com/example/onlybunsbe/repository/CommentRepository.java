package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Comment;
import com.example.onlybunsbe.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByPost(Post post);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post = :post AND c.createdAt > :afterDate")
    long countCommentsAfterDate(Post post, Instant afterDate);
}
