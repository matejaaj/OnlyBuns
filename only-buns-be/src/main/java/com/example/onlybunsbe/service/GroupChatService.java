package com.example.onlybunsbe.service;

import com.example.onlybunsbe.model.GroupChat;
import com.example.onlybunsbe.model.GroupChatMember;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.repository.GroupChatMemberRepository;
import com.example.onlybunsbe.repository.GroupChatRepository;
import com.example.onlybunsbe.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupChatService {

    private final GroupChatRepository groupChatRepository;
    private final UserRepository userRepository;
    private final GroupChatMemberRepository groupChatMemberRepository;

    public GroupChatService(GroupChatRepository groupChatRepository, UserRepository userRepository, GroupChatMemberRepository groupChatMemberRepository) {
        this.groupChatRepository = groupChatRepository;
        this.userRepository = userRepository;
        this.groupChatMemberRepository = groupChatMemberRepository;
    }

    @Transactional
    public GroupChat createGroupChat(String name, Long adminId) {
        Optional<User> adminOptional = userRepository.findById(adminId);
        if (adminOptional.isEmpty()) {
            throw new RuntimeException("Admin user not found");
        }
        User admin = adminOptional.get();

        GroupChat groupChat = new GroupChat();
        groupChat.setName(name);
        groupChat.setAdmin(admin);

        GroupChat savedGroupChat = groupChatRepository.save(groupChat);

        GroupChatMember adminMembership = new GroupChatMember();
        adminMembership.setGroupChat(savedGroupChat);
        adminMembership.setUser(admin);
        adminMembership.setJoinedAt(Instant.now());
        groupChatMemberRepository.save(adminMembership);

        return savedGroupChat;
    }

    @Transactional(readOnly = true)
    public Optional<GroupChat> getGroupChat(Long id) {
        return groupChatRepository.findById(id);
    }

    @Transactional
    public GroupChat addMember(Long groupId, Long userId) {
        Optional<GroupChat> groupChatOptional = groupChatRepository.findById(groupId);
        if (groupChatOptional.isEmpty()) {
            throw new RuntimeException("Group not found");
        }
        GroupChat groupChat = groupChatOptional.get();

        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        if (groupChatMemberRepository.findByGroupChatIdAndUserId(groupId, userId).isPresent()) {
            throw new IllegalArgumentException("User is already a member of this group");
        }

        GroupChatMember membership = new GroupChatMember();
        membership.setGroupChat(groupChat);
        membership.setUser(user);
        membership.setJoinedAt(Instant.now());
        groupChatMemberRepository.save(membership);

        return groupChat;
    }

    @Transactional
    public GroupChat removeMember(Long groupId, Long userId) {
        Optional<GroupChatMember> membershipOptional = groupChatMemberRepository.findByGroupChatIdAndUserId(groupId, userId);
        if (membershipOptional.isEmpty()) {
            throw new RuntimeException("User is not a member of this group");
        }
        groupChatMemberRepository.delete(membershipOptional.get());
        return groupChatRepository.findById(groupId).orElseThrow(() -> new RuntimeException("Group not found"));
    }

    @Transactional
    public void deleteGroupChat(Long groupId) {
        if (!groupChatRepository.existsById(groupId)) {
            throw new RuntimeException("Group not found");
        }
        groupChatRepository.deleteById(groupId);
    }

    @Transactional
    public GroupChat addMemberByEmail(Long groupId, String email) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = userOptional.get();

        Optional<GroupChat> groupChatOptional = groupChatRepository.findById(groupId);
        if (groupChatOptional.isEmpty()) {
            throw new RuntimeException("Group chat not found");
        }
        GroupChat groupChat = groupChatOptional.get();

        if (groupChatMemberRepository.findByGroupChatIdAndUserId(groupId, user.getId()).isPresent()) {
            throw new IllegalArgumentException("User is already a member of this group");
        }

        GroupChatMember membership = new GroupChatMember();
        membership.setGroupChat(groupChat);
        membership.setUser(user);
        membership.setJoinedAt(Instant.now());
        groupChatMemberRepository.save(membership);

        return groupChat;
    }

    @Transactional(readOnly = true)
    public List<User> getGroupMembers(Long groupId) {
        List<GroupChatMember> memberships = groupChatMemberRepository.findAllByGroupChatId(groupId);
        return memberships.stream()
                .map(GroupChatMember::getUser)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GroupChat> getGroupsForUser(Long userId) {
        return groupChatMemberRepository.findAllByUserId(userId).stream()
                .map(GroupChatMember::getGroupChat)
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean isUserNewInGroup(Long groupId, Long userId) {
        return groupChatMemberRepository.findJoinDateByUserIdAndGroupChatId(userId, groupId)
                .map(joinDate -> Instant.now().isBefore(joinDate.plus(Duration.ofMinutes(5)))) // Adjust logic if needed
                .orElse(true);
    }
}
