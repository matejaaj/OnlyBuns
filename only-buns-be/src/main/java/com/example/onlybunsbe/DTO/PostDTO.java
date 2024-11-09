package com.example.onlybunsbe.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private String description;
    private LocalDateTime createdAt;
    private Long userId;
    private ImageDTO image;
    private LocationDTO location;
    private int likeCount;
    private List<CommentDTO> comments;
}
