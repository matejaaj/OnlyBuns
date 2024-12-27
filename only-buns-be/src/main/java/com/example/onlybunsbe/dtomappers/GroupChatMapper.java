package com.example.onlybunsbe.dtomappers;

import com.example.onlybunsbe.DTO.GroupChatDTO;
import com.example.onlybunsbe.model.GroupChat;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.model.GroupChatMember;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GroupChatMapper {

    public GroupChat toGroupChatEntity(GroupChatDTO groupChatDTO) {
        GroupChat groupChat = new GroupChat();
        groupChat.setId(groupChatDTO.getId());
        groupChat.setName(groupChatDTO.getName());

        if (groupChatDTO.getAdminId() != null) {
            User admin = new User();
            admin.setId(groupChatDTO.getAdminId());
            groupChat.setAdmin(admin);
        }

        groupChat.setCreatedAt(groupChatDTO.getCreatedAt());
        return groupChat;
    }

    public static GroupChatDTO toDTO(GroupChat groupChat) {
        GroupChatDTO groupChatDTO = new GroupChatDTO();
        groupChatDTO.setId(groupChat.getId());
        groupChatDTO.setName(groupChat.getName());
        groupChatDTO.setCreatedAt(groupChat.getCreatedAt());

        if (groupChat.getAdmin() != null) {
            groupChatDTO.setAdminId(groupChat.getAdmin().getId());
            groupChatDTO.setAdminUsername(groupChat.getAdmin().getUsername());
        }

        if (groupChat.getMembers() != null) {
            groupChatDTO.setMemberIds(groupChat.getMembers().stream()
                    .map(GroupChatMember::getUser)
                    .map(User::getId)
                    .collect(Collectors.toList()));

            groupChatDTO.setMemberUsernames(groupChat.getMembers().stream()
                    .map(GroupChatMember::getUser)
                    .map(User::getUsername)
                    .collect(Collectors.toList()));
        }

        return groupChatDTO;
    }
}
