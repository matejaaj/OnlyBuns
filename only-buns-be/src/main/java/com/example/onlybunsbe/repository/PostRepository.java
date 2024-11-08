package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
