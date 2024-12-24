package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.GroupChatDTO;
import com.example.onlybunsbe.dtomappers.GroupChatMapper;
import com.example.onlybunsbe.model.GroupChat;
import com.example.onlybunsbe.service.GroupChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/groups")
public class GroupChatController {

    private final GroupChatService groupChatService;

    public GroupChatController(GroupChatService groupChatService) {
        this.groupChatService = groupChatService;
    }

    @PostMapping("/create")
    public ResponseEntity<GroupChatDTO> createGroupChat(@RequestBody GroupChatDTO groupChatDTO) {
        GroupChat groupChat = groupChatService.createGroupChat(groupChatDTO.getName(), groupChatDTO.getAdminId());
        return ResponseEntity.ok(GroupChatMapper.toDTO(groupChat));
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<GroupChatDTO> getGroupChat(@PathVariable Long groupId) {
        Optional<GroupChat> groupChat = groupChatService.getGroupChat(groupId);
        if (groupChat.isPresent()) {
            return ResponseEntity.ok(GroupChatMapper.toDTO(groupChat.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{groupId}/members")
    public ResponseEntity<GroupChatDTO> addMember(@PathVariable Long groupId, @RequestParam Long userId) {
        GroupChat groupChat = groupChatService.addMember(groupId, userId);
        return ResponseEntity.ok(GroupChatMapper.toDTO(groupChat));
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<GroupChatDTO> removeMember(@PathVariable Long groupId, @PathVariable Long userId) {
        GroupChat groupChat = groupChatService.removeMember(groupId, userId);
        return ResponseEntity.ok(GroupChatMapper.toDTO(groupChat));
    }

    @DeleteMapping("/{groupId}")
    public ResponseEntity<Void> deleteGroupChat(@PathVariable Long groupId) {
        groupChatService.deleteGroupChat(groupId);
        return ResponseEntity.noContent().build();
    }
}
