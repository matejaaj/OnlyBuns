package com.example.onlybunsbe.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostDTO {
    private Long id;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private Double latitude;
    private Double longitude;
    private Long userId;
}
