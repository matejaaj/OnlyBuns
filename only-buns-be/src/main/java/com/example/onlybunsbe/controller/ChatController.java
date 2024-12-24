package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.ChatMessageDTO;
import com.example.onlybunsbe.dtomappers.ChatMessageMapper;
import com.example.onlybunsbe.model.ChatMessage;
import com.example.onlybunsbe.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.onlybunsbe.handler.WebSocketHandler;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class ChatController {

    private final ChatMessageService chatMessageService;

    public ChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    // Endpoint za slanje poruke
    @PostMapping
    public ResponseEntity<ChatMessageDTO> sendMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
        if (chatMessageDTO.getGroupId() == null || chatMessageDTO.getSenderId() == null) {
            throw new IllegalArgumentException("Group ID and Sender ID must not be null");
        }
        System.out.println("Received message DTO: " + chatMessageDTO);

        // Mapiranje DTO-a u entitet i čuvanje u bazi
        ChatMessage chatMessage = ChatMessageMapper.toEntity(chatMessageDTO);
        System.out.println("Mapped ChatMessage entity: " + chatMessage);

        ChatMessage savedMessage = chatMessageService.saveMessage(chatMessage);
        System.out.println("Saved ChatMessage entity: " + savedMessage);

        // Emitovanje poruke svim povezanim klijentima
        WebSocketHandler.broadcastMessage(ChatMessageMapper.toChatMessageDTO(savedMessage));

        // Vraćanje sačuvane poruke kao odgovor
        ChatMessageDTO responseDto = ChatMessageMapper.toChatMessageDTO(savedMessage);
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
}
