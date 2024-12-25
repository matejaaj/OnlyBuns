package com.example.onlybunsbe.dtomappers;

import com.example.onlybunsbe.DTO.UserDTO;
import com.example.onlybunsbe.model.ChatMessage;
import com.example.onlybunsbe.model.GroupChat;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.model.GroupChatMember;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();

        // Osnovna polja
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAddress(user.getAddress());

        // Datum registracije
        dto.setRegisteredAt(user.getLastLoginDate() != null
                ? LocalDateTime.ofInstant(user.getLastLoginDate(), ZoneId.systemDefault())
                : null);

        // Rola korisnika
        dto.setRole(user.getRole().getName());

        // Statistika
        dto.setPostCount(user.getPosts() != null ? user.getPosts().size() : 0);
        dto.setFollowingCount(user.getFollowing() != null ? user.getFollowing().size() : 0);
        dto.setFollowerCount(user.getFollowers() != null ? user.getFollowers().size() : 0);

        // Poruke
        dto.setSentMessageIds(user.getSentMessages() != null
                ? user.getSentMessages().stream().map(ChatMessage::getId).collect(Collectors.toList())
                : List.of());

        // Grupe
        return dto;
    }
}
