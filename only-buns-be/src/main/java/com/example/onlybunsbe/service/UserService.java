package com.example.onlybunsbe.service;

import java.util.List;
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
}