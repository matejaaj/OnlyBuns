package com.example.onlybunsbe.DTO;

import lombok.Data;

@Data
public class PetCareLocationDTO {
    private Long id;
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
}
