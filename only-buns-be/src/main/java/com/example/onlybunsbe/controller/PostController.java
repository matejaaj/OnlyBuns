package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.CommentDTO;
import com.example.onlybunsbe.DTO.PostDTO;
import com.example.onlybunsbe.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/{postId}/like")
    public ResponseEntity<Void> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        if (postService.likePost(postId, userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(409).build(); // Conflict ako je već lajkovano
    }

    @PostMapping("/{postId}/comment")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId, @RequestParam Long userId, @RequestBody String content) {
        Optional<CommentDTO> comment = postService.addComment(postId, userId, content);
        return comment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long postId, @RequestParam Long userId, @RequestBody String newDescription) {
        Optional<PostDTO> updatedPost = postService.updatePost(postId, userId, newDescription);
        return updatedPost.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(403).build()); // Forbidden ako korisnik nije vlasnik
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @RequestParam Long userId) {
        if (postService.deletePost(postId, userId)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(403).build(); // Forbidden ako korisnik nije vlasnik
    }
}
