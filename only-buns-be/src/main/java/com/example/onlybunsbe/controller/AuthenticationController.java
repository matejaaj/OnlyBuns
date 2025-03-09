package com.example.onlybunsbe.controller;

import com.example.onlybunsbe.DTO.JwtAuthenticationRequest;
import com.example.onlybunsbe.DTO.UserRequest;
import com.example.onlybunsbe.DTO.UserTokenState;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody JwtAuthenticationRequest authenticationRequest,
            HttpServletRequest request) {
        return authService.login(authenticationRequest, request);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {
        User user = authService.register(userRequest);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("token") String token) {
        boolean isActivated = authService.activateUser(token);
        if (isActivated) {
            return ResponseEntity.ok("Account activated successfully. You can now log in.");
        } else {
            return ResponseEntity.badRequest().body("Invalid activation token.");
        }
    }
}
