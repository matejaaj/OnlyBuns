package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Like;
import com.example.onlybunsbe.model.Post;
import com.example.onlybunsbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByPostAndUser(Post post, User user);

    void deleteByPost(Post post);
}
