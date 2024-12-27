package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.FollowDTO;
import com.example.onlybunsbe.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/follow")
@CrossOrigin
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> followUser(@PathVariable Long userId, Principal principal) {
        try {
            followService.followUser(principal.getName(), userId);
            return ResponseEntity.ok("Started following user");
        } catch (RuntimeException e) {
            return ResponseEntity.status(429).body(e.getMessage()); // 429 Too Many Requests
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long userId, Principal principal) {
        try {
            followService.unfollowUser(principal.getName(), userId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<List<FollowDTO>> getFollowers(@PathVariable Long userId) {
        List<FollowDTO> followers = followService.getFollowers(userId);
        if (followers.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<List<FollowDTO>> getFollowing(@PathVariable Long userId) {
        List<FollowDTO> following = followService.getFollowing(userId);
        if (following.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(following);
    }
}
