package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc(); // Dodaje sortiranje postova po datumu kreiranja
}


