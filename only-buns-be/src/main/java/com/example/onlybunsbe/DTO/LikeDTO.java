package com.example.onlybunsbe.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LikeDTO {
    private Long id;
    private Long postId;
    private Long userId;
    private LocalDateTime likedAt;
}