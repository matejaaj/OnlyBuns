package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Like;
import com.example.onlybunsbe.model.Post;
import com.example.onlybunsbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostAndUser(Post post, User user);

    void deleteByPost(Post post);

    @Query("SELECT COUNT(l) FROM Like l WHERE l.post = :post AND l.likedAt > :afterDate")
    long countLikesAfterDate(Post post, Instant afterDate);
}
