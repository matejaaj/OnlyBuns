package com.example.onlybunsbe.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FollowDTO {
    private Long id;
    private Long followerId;
    private String followerUsername;
    private Long followedId;
    private String followedUsername;
    private LocalDateTime followedAt;
}
