package com.example.onlybunsbe.DTO;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class GroupChatDTO {
    private Long id;                // ID grupe
    private String name;            // Ime grupe
    private Long adminId;           // ID admina
    private String adminUsername;   // Korisničko ime admina
    private List<Long> memberIds;   // Lista ID-eva članova
    private List<String> memberUsernames; // Lista korisničkih imena članova
    private Instant createdAt;      // Datum kreiranja grupe
}
