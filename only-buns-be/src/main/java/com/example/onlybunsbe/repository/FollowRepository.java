package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Follow;
import com.example.onlybunsbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowed(User follower, User followed);
    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);
    List<Follow> findAllByFollower(User follower);
    List<Follow> findAllByFollowed(User followed);
}