package com.example.onlybunsbe.service;

import com.example.onlybunsbe.model.ChatMessage;
import com.example.onlybunsbe.repository.ChatMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Transactional
    public ChatMessage saveMessage(ChatMessage chatMessage) {
        System.out.println("Saving ChatMessage entity: " + chatMessage);
        return chatMessageRepository.save(chatMessage);
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getMessagesByGroup(Long groupId) {
        return chatMessageRepository.findByGroupIdOrderByCreatedAtDesc(groupId);
    }

}
