package com.example.onlybunsbe.DTO;

import lombok.Data;

@Data
public class LocationDTO {
    private String country;
    private String city;
    private String address;
    private int number;
    private double latitude;
    private double longitude;
}
