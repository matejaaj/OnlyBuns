package com.example.onlybunsbe.service;

import com.example.onlybunsbe.repository.CommentRepository;
import com.example.onlybunsbe.repository.PostRepository;
import com.example.onlybunsbe.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class AnalyticsService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public AnalyticsService(PostRepository postRepository, CommentRepository commentRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> getPostsAndCommentsStats() {
        Instant now = Instant.now();

        // Broj objava
        long weeklyPosts = postRepository.countPostsAfterDate(now.minusSeconds(7 * 24 * 60 * 60)); // 7 dana
        long monthlyPosts = postRepository.countPostsAfterDate(now.minusSeconds(30L * 24 * 60 * 60)); // 30 dana
        long yearlyPosts = postRepository.countPostsAfterDate(now.minusSeconds(365L * 24 * 60 * 60)); // 365 dana

        // Broj komentara
        long weeklyComments = commentRepository.countCommentsAfterDate(now.minusSeconds(7 * 24 * 60 * 60));
        long monthlyComments = commentRepository.countCommentsAfterDate(now.minusSeconds(30L * 24 * 60 * 60));
        long yearlyComments = commentRepository.countCommentsAfterDate(now.minusSeconds(365L * 24 * 60 * 60));

        Map<String, Object> stats = new HashMap<>();
        stats.put("weeklyPosts", weeklyPosts);
        stats.put("monthlyPosts", monthlyPosts);
        stats.put("yearlyPosts", yearlyPosts);
        stats.put("weeklyComments", weeklyComments);
        stats.put("monthlyComments", monthlyComments);
        stats.put("yearlyComments", yearlyComments);

        return stats;
    }


    public Map<String, Object> getUserActivityStats() {
        long totalUsers = userRepository.count();
        long usersWithPosts = userRepository.countByPostsIsNotEmpty();
        long usersWithOnlyComments = userRepository.countWithOnlyComments();
        long usersWithoutActivity = totalUsers - usersWithPosts - usersWithOnlyComments;

        Map<String, Object> activityStats = new HashMap<>();
        activityStats.put("madePosts", (usersWithPosts * 100.0) / totalUsers);
        activityStats.put("madeOnlyComments", (usersWithOnlyComments * 100.0) / totalUsers);
        activityStats.put("noActivity", (usersWithoutActivity * 100.0) / totalUsers);

        return activityStats;
    }
}