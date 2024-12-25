package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.GroupChatMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupChatMemberRepository extends JpaRepository<GroupChatMember, Long> {

    Optional<GroupChatMember> findByGroupChatIdAndUserId(Long groupId, Long userId);

    @Query("SELECT gcm.joinedAt FROM GroupChatMember gcm WHERE gcm.user.id = :userId AND gcm.groupChat.id = :groupId")
    Optional<Instant> findJoinDateByUserIdAndGroupChatId(@Param("userId") Long userId, @Param("groupId") Long groupId);


    List<GroupChatMember> findAllByGroupChatId(Long groupId);

    List<GroupChatMember> findAllByUserId(Long userId);
}
