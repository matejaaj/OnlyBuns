package com.example.onlybunsbe.service;

import com.example.onlybunsbe.DTO.CommentDTO;
import com.example.onlybunsbe.model.Comment;
import com.example.onlybunsbe.model.Post;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.repository.CommentRepository;
import com.example.onlybunsbe.repository.PostRepository;
import com.example.onlybunsbe.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Optional<CommentDTO> addComment(Long postId, Long userId, String content) {
        Optional<Post> post = postRepository.findById(postId);
        Optional<User> user = userRepository.findById(userId);

        if (post.isPresent() && user.isPresent()) {
            Comment comment = new Comment();
            comment.setPost(post.get());
            comment.setUser(user.get());
            comment.setContent(content);
            comment.setCreatedAt(Instant.now());
            commentRepository.save(comment);

            // Mapiranje komentara u CommentDTO
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId((long) comment.getId());
            commentDTO.setContent(content);
            commentDTO.setCreatedAt(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());
            commentDTO.setUserName(user.get().getUsername());

            return Optional.of(commentDTO);
        }
        return Optional.empty();
    }

    @Transactional
    public void removeCommentsByPost(Post post) {
        commentRepository.deleteByPost(post);
    }
}
