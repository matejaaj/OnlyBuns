package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.ChatMessageDTO;
import com.example.onlybunsbe.dtomappers.ChatMessageMapper;
import com.example.onlybunsbe.model.ChatMessage;
import com.example.onlybunsbe.service.ChatMessageService;
import com.example.onlybunsbe.service.GroupChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.onlybunsbe.handler.WebSocketHandler;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final GroupChatService groupChatService;

    public ChatController(ChatMessageService chatMessageService, GroupChatService groupChatService) {
        this.chatMessageService = chatMessageService;
        this.groupChatService = groupChatService;
    }

    // Endpoint za slanje poruke
    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
        // Provera da li je korisnik član grupe
        boolean isMember = groupChatService.isUserMemberOfGroup(chatMessageDTO.getGroupId(), chatMessageDTO.getSenderId());

        if (!isMember) {
            return ResponseEntity.status(403).body("You are no longer a member of this group.");
        }

        ChatMessage chatMessage = ChatMessageMapper.toEntity(chatMessageDTO);
        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);
        ChatMessageDTO responseDto = ChatMessageMapper.toChatMessageDTO(savedMessage);
        WebSocketHandler.broadcastMessage(responseDto);

        return ResponseEntity.ok(responseDto);
    }

    // Endpoint za dohvat poruka za određenu grupu
    @GetMapping("/{groupId}")
    public List<ChatMessageDTO> getMessagesByGroup(@PathVariable Long groupId) {
        return chatMessageService.getMessagesByGroup(groupId)
                .stream()
                .map(ChatMessageMapper::toChatMessageDTO)
                .toList();
    }

    @GetMapping("/{groupId}/recent-messages")
    public List<ChatMessageDTO> getRecentMessages(@PathVariable Long groupId, @RequestParam Long userId) {
        boolean isNewUser = groupChatService.isUserNewInGroup(groupId, userId);

        if (isNewUser) {
            // Ako je korisnik nov, vraćamo poslednjih 10 poruka pre nego što se pridružio
            List<ChatMessage> recentMessages = chatMessageService.getRecentMessagesBeforeJoin(groupId, userId, 10);
            return recentMessages.stream()
                    .map(ChatMessageMapper::toChatMessageDTO)
                    .collect(Collectors.toList());
        } else {
            // Ako nije nov korisnik, vraćamo sve poruke za grupu
            List<ChatMessage> allMessages = chatMessageService.getMessagesByGroup(groupId);
            return allMessages.stream()
                    .map(ChatMessageMapper::toChatMessageDTO)
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/{groupId}/messages-after")
    public List<ChatMessageDTO> getMessagesAfter(
            @PathVariable Long groupId,
            @RequestParam Instant afterTimestamp) {
        List<ChatMessage> messages = chatMessageService.getMessagesAfter(groupId, afterTimestamp);
        return messages.stream()
                .map(ChatMessageMapper::toChatMessageDTO)
                .collect(Collectors.toList());
    }

}
