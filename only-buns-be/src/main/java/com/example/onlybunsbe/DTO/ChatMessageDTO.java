package com.example.onlybunsbe.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ChatMessageDTO {
    private Long id;
    private Long senderId;
    private String senderUsername;
    private Long groupId;
    private String groupName;
    private String content;
    private String type;
    private Instant createdAt;
}
