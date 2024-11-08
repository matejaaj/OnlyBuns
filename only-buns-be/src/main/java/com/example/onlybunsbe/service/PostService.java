package com.example.onlybunsbe.service;

import com.example.onlybunsbe.DTO.CommentDTO;
import com.example.onlybunsbe.DTO.PostDTO;
import com.example.onlybunsbe.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private PostRepository postRepository;

    public Optional<PostDTO> getPostById(Long id) {
        return postRepository.findById(id).map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(Long.valueOf(post.getId()));
            dto.setDescription(post.getDescription());
            dto.setImageUrl(post.getImageUrl());
            dto.setCreatedAt(LocalDateTime.from(post.getCreatedAt()));
            dto.setLatitude(post.getLatitude());
            dto.setLongitude(post.getLongitude());
            dto.setUserId(Long.valueOf(post.getUser().getId()));
            dto.setLikeCount(post.getLikes().size());
            dto.setComments(post.getComments().stream().map(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setId(Long.valueOf(comment.getId()));
                commentDTO.setContent(comment.getContent());
                commentDTO.setCreatedAt(LocalDateTime.from(comment.getCreatedAt()));
                commentDTO.setUserName(comment.getUser().getUsername());
                return commentDTO;
            }).collect(Collectors.toList()));
            return dto;
        });
    }
}
