package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.UserDTO;
import com.example.onlybunsbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint za dobijanje svih korisnika sa opcijom sortiranja
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(value = "sortBy", defaultValue = "email") String sortBy,
            @RequestParam(value = "isAscending", defaultValue = "true") boolean isAscending) {
        List<UserDTO> users = userService.getAllUsers(sortBy, isAscending);
        return ResponseEntity.ok(users);
    }

}