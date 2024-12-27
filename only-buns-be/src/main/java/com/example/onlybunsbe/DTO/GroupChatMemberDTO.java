package com.example.onlybunsbe.DTO;

import lombok.Data;

import java.time.Instant;

@Data
public class GroupChatMemberDTO {
    private Long id;               // ID članstva
    private Long groupChatId;      // ID grupnog četa
    private Long userId;           // ID korisnika
    private Instant joinedAt;      // Datum pridruživanja grupi
}
