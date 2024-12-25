package com.example.onlybunsbe.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "group_chat_members")
public class GroupChatMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_chat_id", nullable = false)
    private GroupChat groupChat; // Grupni čet

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Korisnik

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Instant joinedAt = Instant.now(); // Datum pridruživanja
}
