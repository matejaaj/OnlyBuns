package com.example.onlybunsbe.service;

import com.example.onlybunsbe.model.GroupChat;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.repository.GroupChatRepository;
import com.example.onlybunsbe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final UserRepository userRepository;

    public GroupChatService(GroupChatRepository groupChatRepository, UserRepository userRepository) {
        this.groupChatRepository = groupChatRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public GroupChat createGroupChat(String name, Long adminId) {
        // Pronađi admina u bazi podataka
        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        // Kreiraj grupni čet
        GroupChat groupChat = new GroupChat();
        groupChat.setName(name);
        groupChat.setAdmin(admin);
        groupChat.getMembers().add(admin);

        return groupChatRepository.save(groupChat);
    }

    @Transactional(readOnly = true)
    public Optional<GroupChat> getGroupChat(Long id) {
        return groupChatRepository.findById(id);
    }

    @Transactional
    public GroupChat addMember(Long groupId, Long userId) {
        // Pronađi grupni čet
        GroupChat groupChat = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Pronađi korisnika
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Dodaj korisnika u grupu
        groupChat.getMembers().add(user);
        return groupChatRepository.save(groupChat);
    }

    @Transactional
    public GroupChat removeMember(Long groupId, Long userId) {
        // Pronađi grupni čet
        GroupChat groupChat = groupChatRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        // Pronađi korisnika
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Ukloni korisnika iz grupe
        if (groupChat.getMembers().contains(user)) {
            groupChat.getMembers().remove(user);
        } else {
            throw new RuntimeException("User is not a member of this group");
        }

        return groupChatRepository.save(groupChat);
    }

    @Transactional
    public void deleteGroupChat(Long groupId) {
        // Pronađi grupu i izbriši je
        if (!groupChatRepository.existsById(groupId)) {
            throw new RuntimeException("Group not found");
        }
        groupChatRepository.deleteById(groupId);
    }
}
