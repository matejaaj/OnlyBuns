package com.example.onlybunsbe.dtomappers;

import com.example.onlybunsbe.DTO.FollowDTO;
import com.example.onlybunsbe.model.Follow;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class FollowMapper {

    public static FollowDTO toDTO(Follow follow) {
        FollowDTO dto = new FollowDTO();
        dto.setId(follow.getId());
        dto.setFollowerId(follow.getFollower().getId());
        dto.setFollowerUsername(follow.getFollower().getUsername());
        dto.setFollowedId(follow.getFollowed().getId());
        dto.setFollowedUsername(follow.getFollowed().getUsername());
        dto.setFollowedAt(LocalDateTime.ofInstant(follow.getFollowedAt(), ZoneId.systemDefault()));
        return dto;
    }
}
