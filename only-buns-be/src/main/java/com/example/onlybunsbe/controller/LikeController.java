package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.LikeDTO;
import com.example.onlybunsbe.DTO.PostDTO;
import com.example.onlybunsbe.service.LikeService;
import com.example.onlybunsbe.util.TokenUtils;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private TokenUtils tokenUtils;

    // Dodavanje lajka na objavu
    @PostMapping("/{postId}")
    public ResponseEntity<PostDTO> likePost(@PathVariable Long postId, HttpServletRequest request) {
        long userId = tokenUtils.getUserIdFromToken(tokenUtils.getToken(request));

        System.out.println("Request received to like post with ID: " + postId + " by user ID: " + userId);

        PostDTO post = likeService.likePost(postId, userId);
        if (post != null) {
            System.out.println("Post successfully liked.");
            return ResponseEntity.ok(post); // Vraćamo PostDTO kao telo odgovora
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
