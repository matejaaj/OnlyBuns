package com.example.onlybunsbe.dtomappers;

import com.example.onlybunsbe.DTO.ChatMessageDTO;
import com.example.onlybunsbe.model.ChatMessage;
import com.example.onlybunsbe.model.GroupChat;
import com.example.onlybunsbe.model.User;

import java.time.Instant;

public class ChatMessageMapper {

    public static ChatMessage toEntity(ChatMessageDTO chatMessageDTO) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(chatMessageDTO.getContent());
        chatMessage.setCreatedAt(chatMessageDTO.getCreatedAt());

        if (chatMessageDTO.getSenderId() != null) {
            User sender = new User();
            sender.setId(chatMessageDTO.getSenderId());
            chatMessage.setSender(sender);
        }

        if (chatMessageDTO.getGroupId() != null) {
            GroupChat groupChat = new GroupChat();
            groupChat.setId(chatMessageDTO.getGroupId());
            chatMessage.setGroup(groupChat);
        }

        chatMessage.setType(chatMessageDTO.getType() != null
                ? ChatMessage.MessageType.valueOf(chatMessageDTO.getType())
                : ChatMessage.MessageType.CHAT); // Default na CHAT ako nije prosleđeno

        if (chatMessageDTO.getCreatedAt() != null) {
            chatMessage.setCreatedAt(chatMessageDTO.getCreatedAt());
        }

        return chatMessage;
    }


    public static ChatMessageDTO toChatMessageDTO(ChatMessage chatMessage) {
        ChatMessageDTO chatMessageDTO = new ChatMessageDTO();
        chatMessageDTO.setId(chatMessage.getId());
        chatMessageDTO.setContent(chatMessage.getContent());
        chatMessageDTO.setCreatedAt(chatMessage.getCreatedAt());

        if (chatMessage.getSender() != null) {
            chatMessageDTO.setSenderId(chatMessage.getSender().getId());
            chatMessageDTO.setSenderUsername(chatMessage.getSender().getUsername());
        }

        if (chatMessage.getGroup() != null) {
            chatMessageDTO.setGroupId(chatMessage.getGroup().getId());
            chatMessageDTO.setGroupName(chatMessage.getGroup().getName());
        }

        chatMessageDTO.setType(chatMessage.getType().name());
        return chatMessageDTO;
    }
}
