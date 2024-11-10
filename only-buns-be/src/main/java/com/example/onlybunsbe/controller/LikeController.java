package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.LikeDTO;
import com.example.onlybunsbe.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // Dodavanje lajka na objavu
    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        System.out.println("Request received to like post with ID: " + postId + " by user ID: " + userId);

        boolean liked = likeService.likePost(postId, userId);
        if (liked) {
            System.out.println("Post successfully liked.");
            return ResponseEntity.ok().build();
        }
        System.out.println("Conflict: Post already liked by this user.");
        return ResponseEntity.status(409).build();
    }

    @GetMapping
    public ResponseEntity<List<LikeDTO>> getAllLikes() {
        System.out.println("Request received to GET ALL LIKES");
        List<LikeDTO> likes = likeService.getAllLikes();
        return ResponseEntity.ok(likes);
    }
}
