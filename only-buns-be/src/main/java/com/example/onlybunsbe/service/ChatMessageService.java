package com.example.onlybunsbe.service;

import com.example.onlybunsbe.model.ChatMessage;
import com.example.onlybunsbe.repository.ChatMessageRepository;
import com.example.onlybunsbe.repository.GroupChatMemberRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final GroupChatMemberRepository groupChatMemberRepository;

    public ChatMessageService(ChatMessageRepository chatMessageRepository,
                              GroupChatMemberRepository groupChatMemberRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.groupChatMemberRepository = groupChatMemberRepository;
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

    @Transactional(readOnly = true)
    public List<ChatMessage> getRecentMessagesBeforeJoin(Long groupId, Long userId, int limit) {
        // Pronađi datum pridruživanja korisnika grupi
        Instant joinedAt = groupChatMemberRepository
                .findJoinDateByUserIdAndGroupChatId(userId, groupId)
                .orElseThrow(() -> new EntityNotFoundException("User is not a member of the group"));

        System.out.println("User joined at: " + joinedAt);

        // Pronađi poruke poslate pre nego što je korisnik postao član grupe
        Pageable pageable = PageRequest.of(0, limit);
        List<ChatMessage> messages = chatMessageRepository.findMessagesBefore(groupId, joinedAt, pageable);

        System.out.println("Fetched recent messages for user " + userId + ": " + messages.size());
        return messages;
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getMessagesAfter(Long groupId, Instant afterTimestamp) {
        return chatMessageRepository.findMessagesAfter(groupId, afterTimestamp);
    }
}
