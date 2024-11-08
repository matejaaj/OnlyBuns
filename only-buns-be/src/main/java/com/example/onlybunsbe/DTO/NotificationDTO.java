package com.example.onlybunsbe.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String content;
    private LocalDateTime sentAt;
    private boolean isRead;
}
