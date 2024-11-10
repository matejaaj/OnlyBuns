package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.CommentDTO;
import com.example.onlybunsbe.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // Dodavanje komentara na objavu
    @PostMapping("/{postId}")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId, @RequestParam Long userId, @RequestBody CommentDTO commentRequest) {
        String content = commentRequest.getContent();
        System.out.println("Request received to add comment to post with ID: " + postId + " by user ID: " + userId);
        Optional<CommentDTO> commentDTO = commentService.addComment(postId, userId, content);
        return commentDTO.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    System.out.println("Comment could not be added - post or user not found");
                    return ResponseEntity.notFound().build();
                });
    }
}
