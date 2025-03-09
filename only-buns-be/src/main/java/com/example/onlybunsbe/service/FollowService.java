package com.example.onlybunsbe.service;

import com.example.onlybunsbe.DTO.FollowDTO;
import com.example.onlybunsbe.dtomappers.FollowMapper;
import com.example.onlybunsbe.model.Follow;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.repository.FollowRepository;
import com.example.onlybunsbe.repository.UserRepository;
import jakarta.persistence.OptimisticLockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RateLimiterService rateLimiterService;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void followUser(String currentUsername, Long userIdToFollow) {
        // Proveri rate limiting
        if (!rateLimiterService.isAllowed(currentUsername)) {
            throw new RuntimeException("Rate limit exceeded. Please try again later.");
        }

        // Pronađi trenutnog korisnika
        User currentUser = userRepository.findByUsername(currentUsername);

        // Pronađi korisnika koji se prati
        User userToFollow = userRepository.findById(userIdToFollow)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Proveri da li korisnik već prati
        if (followRepository.existsByFollowerAndFollowed(currentUser, userToFollow)) {
            throw new RuntimeException("Already following this user");
        }

        // Simulacija kašnjenja da se poveća šansa za konkurentni pristup
//        try {
//            Thread.sleep(100); // 100ms pauze
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt(); // Reset interrupt flag
//            throw new RuntimeException("Thread interrupted", e);
//        }

        // Kreiraj novi Follow entitet
        Follow follow = new Follow();
        follow.setFollower(currentUser);
        follow.setFollowed(userToFollow);

        followRepository.save(follow);
    }


    @Transactional
    public void unfollowUser(String currentUsername, Long userIdToUnfollow) {
        User currentUser = userRepository.findByUsername(currentUsername);
        User userToUnfollow = userRepository.findById(userIdToUnfollow)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Follow follow = followRepository.findByFollowerAndFollowed(currentUser, userToUnfollow)
                .orElseThrow(() -> new RuntimeException("Not following this user"));

        followRepository.delete(follow);
    }

    public List<FollowDTO> getFollowers(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return followRepository.findAllByFollowed(user).stream()
                .map(FollowMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<FollowDTO> getFollowing(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return followRepository.findAllByFollower(user).stream()
                .map(FollowMapper::toDTO)
                .collect(Collectors.toList());
    }
}
