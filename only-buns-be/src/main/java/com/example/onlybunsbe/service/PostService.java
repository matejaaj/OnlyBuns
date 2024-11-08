package com.example.onlybunsbe.service;

import com.example.onlybunsbe.DTO.CommentDTO;
import com.example.onlybunsbe.DTO.PostDTO;
import com.example.onlybunsbe.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;
    @Transactional
    public Optional<PostDTO> getPostById(Long id) {
        return postRepository.findById(id).map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(Long.valueOf(post.getId()));
            dto.setDescription(post.getDescription());
            dto.setImageUrl(post.getImageUrl());

            // Konvertujemo Instant u LocalDateTime
            dto.setCreatedAt(post.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());

            dto.setLatitude(post.getLatitude());
            dto.setLongitude(post.getLongitude());
            dto.setUserId(post.getUser() != null ? Long.valueOf(post.getUser().getId()) : null); // Provera null vrednosti
            dto.setLikeCount(post.getLikes() != null ? post.getLikes().size() : 0);
            dto.setComments(post.getComments() != null ? post.getComments().stream().map(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setId(Long.valueOf(comment.getId()));
                commentDTO.setContent(comment.getContent());

                // Konvertujemo Instant u LocalDateTime za comment
                commentDTO.setCreatedAt(comment.getCreatedAt().atZone(ZoneId.systemDefault()).toLocalDateTime());

                commentDTO.setUserName(comment.getUser() != null ? comment.getUser().getUsername() : ""); // Provera null vrednosti
                return commentDTO;
            }).collect(Collectors.toList()) : new ArrayList<>());
            return dto;
        });
    }

}
