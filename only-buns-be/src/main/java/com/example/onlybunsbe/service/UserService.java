package com.example.onlybunsbe.service;

import com.example.onlybunsbe.DTO.UserDTO;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    // Metoda za dobijanje liste svih korisnika sa opcijom sortiranja
    public List<UserDTO> getAllUsers(String sortBy, boolean isAscending) {
        List<User> users = userRepository.findAll();

        // Primeni sortiranje na osnovu parametra
        Comparator<User> comparator;
        if ("followers".equals(sortBy)) {
            comparator = Comparator.comparingInt(user -> user.getFollowers().size());
        } else {
            comparator = Comparator.comparing(User::getEmail);
        }

        // Obrni redosled ako nije rastuće
        if (!isAscending) {
            comparator = comparator.reversed();
        }

        // Sortiraj i konvertuj korisnike u DTO
        return users.stream()
                .sorted(comparator)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(Long.valueOf(user.getId()));
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setAddress(user.getAddress());
        dto.setActive(user.getIsActive());
        dto.setRegisteredAt(user.getRegisteredAt().atZone(ZoneId.systemDefault()).toLocalDateTime());
        dto.setRole(user.getRole());
        dto.setPostCount(user.getPosts() != null ? user.getPosts().size() : 0);
        dto.setFollowingCount(user.getFollowing() != null ? user.getFollowing().size() : 0);
        dto.setFollowerCount(user.getFollowers() != null ? user.getFollowers().size() : 0);
        return dto;
    }
}
