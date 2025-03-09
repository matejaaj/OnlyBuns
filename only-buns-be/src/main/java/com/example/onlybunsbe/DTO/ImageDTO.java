package com.example.onlybunsbe.DTO;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
public class ImageDTO {
    private Long id;                 // Dodajemo id za jedinstvenu identifikaciju slike
    private String path;             // Putanja slike
    private boolean isCompressed;     // Indikacija da li je slika kompresovana
    private LocalDateTime uploadedAt; // Datum i vreme kada je slika postavljena
}
