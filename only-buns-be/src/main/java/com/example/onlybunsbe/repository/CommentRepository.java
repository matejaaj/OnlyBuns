package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Comment;
import com.example.onlybunsbe.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteByPost(Post post);
}
