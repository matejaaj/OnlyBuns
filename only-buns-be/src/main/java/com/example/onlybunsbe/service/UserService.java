package com.example.onlybunsbe.service;

import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.onlybunsbe.DTO.UserDTO;
import com.example.onlybunsbe.DTO.UserRequest;
import com.example.onlybunsbe.model.Role;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    // Pronalazi korisnika po korisničkom imenu
    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email);
    }

    // Pronalazi korisnika po ID-u
    public User findById(Long id) throws AccessDeniedException {
        return userRepository.findById(id).orElse(null);
    }

    // Pronalazi sve korisnike
    public List<User> findAll() throws AccessDeniedException {
        return userRepository.findAll();
    }

    // Čuva novog korisnika na osnovu UserRequest DTO-a
    public User save(UserRequest userRequest) {
        User u = new User();
        u.setUsername(userRequest.getUsername());

        // Hesira lozinku pre čuvanja u bazi
        u.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        u.setFirstName(userRequest.getFirstname());
        u.setLastName(userRequest.getLastname());
        u.setEnabled(true);
        u.setEmail(userRequest.getEmail());
        u.setAddress(userRequest.getAddress());
        Role role = roleService.findByName("ROLE_USER").orElseThrow(() -> new RuntimeException("Role 'ROLE_USER' not found"));
        u.setRole(role);

        return this.userRepository.save(u);
    }
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
        dto.setRole(user.getRole().getName());
        dto.setPostCount(user.getPosts() != null ? user.getPosts().size() : 0);
        dto.setFollowingCount(user.getFollowing() != null ? user.getFollowing().size() : 0);
        dto.setFollowerCount(user.getFollowers() != null ? user.getFollowers().size() : 0);
        return dto;
    }
}

