package com.example.onlybunsbe.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private Long senderId;
    private Long recipientId;
    private boolean isRead;
}
