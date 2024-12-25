package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByGroupIdOrderByCreatedAtDesc(Long groupId);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.group.id = :groupId AND cm.createdAt < :joinedAt ORDER BY cm.createdAt DESC")
    List<ChatMessage> findMessagesBefore(@Param("groupId") Long groupId, @Param("joinedAt") Instant joinedAt, Pageable pageable);

    @Query("SELECT cm FROM ChatMessage cm WHERE cm.group.id = :groupId AND cm.createdAt > :afterTimestamp ORDER BY cm.createdAt ASC")
    List<ChatMessage> findMessagesAfter(@Param("groupId") Long groupId, @Param("afterTimestamp") Instant afterTimestamp);
}