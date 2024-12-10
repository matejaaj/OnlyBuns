package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.CommentDTO;
import com.example.onlybunsbe.service.CommentService;
import com.example.onlybunsbe.util.LimiterManager;
import com.example.onlybunsbe.util.TokenUtils;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.vavr.control.Try;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.function.Supplier;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private LimiterManager limiterManager;

    // Dodavanje komentara na objavu
    @PostMapping("/{postId}")
    public ResponseEntity<?> addComment(@PathVariable Long postId, @RequestBody CommentDTO commentDTO, HttpServletRequest request) {
        long userId = tokenUtils.getUserIdFromToken(tokenUtils.getToken(request));
        commentDTO.setUserId(userId);
        commentDTO.setPostId(postId);



        String userKey = String.valueOf(userId);
        final RateLimiter rateLimiter = limiterManager.getLimiter(userKey);

        Supplier<ResponseEntity<?>> supplier = RateLimiter
                .decorateSupplier(rateLimiter, () -> {
                    Optional<CommentDTO> result = commentService.addComment(commentDTO);
                    return result.map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
                });

        return Try.ofSupplier(supplier)
                .recover(RequestNotPermitted.class, error -> {
                    System.out.println("User " + userId + " je prekoračio limit komentara.");
                    return ResponseEntity.status(429).body(null);
                })
                .get();
    }

}
