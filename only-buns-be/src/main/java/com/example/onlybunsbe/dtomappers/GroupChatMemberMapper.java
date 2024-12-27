package com.example.onlybunsbe.dtomappers;

import com.example.onlybunsbe.DTO.GroupChatMemberDTO;
import com.example.onlybunsbe.model.GroupChatMember;

public class GroupChatMemberMapper {

    public static GroupChatMemberDTO toDTO(GroupChatMember groupChatMember) {
        GroupChatMemberDTO dto = new GroupChatMemberDTO();
        dto.setId(groupChatMember.getId());
        dto.setGroupChatId(groupChatMember.getGroupChat().getId());
        dto.setUserId(groupChatMember.getUser().getId());
        dto.setJoinedAt(groupChatMember.getJoinedAt());
        return dto;
    }
}
