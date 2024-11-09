package com.example.onlybunsbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "content", nullable = false, length = Integer.MAX_VALUE)
    private String content;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "sent_at", nullable = false)
    private Instant sentAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "sender_id")
    private com.example.onlybunsbe.model.User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "recipient_id")
    private com.example.onlybunsbe.model.User recipient;

    @ColumnDefault("false")
    @Column(name = "is_read")
    private Boolean isRead;

}