package com.example.onlybunsbe.DTO;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {
    private Long id;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Double latitude;
    private Double longitude;
    private Long userId;
    private int likeCount; // Dodato polje za broj lajkova
    private List<CommentDTO> comments; // Lista komentara
}
