package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    // Dodavanje lajka na objavu
    @PostMapping("/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        boolean liked = likeService.likePost(postId, userId);
        if (liked) {
            return ResponseEntity.ok().build(); // Uspešno lajkovano
        }
        return ResponseEntity.status(409).build(); // Conflict ako je već lajkovano
    }
}
