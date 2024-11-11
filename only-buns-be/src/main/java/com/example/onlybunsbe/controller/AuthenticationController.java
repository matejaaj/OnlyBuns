package com.example.onlybunsbe.controller;

import jakarta.servlet.http.HttpServletResponse;

import com.example.onlybunsbe.DTO.JwtAuthenticationRequest;
import com.example.onlybunsbe.DTO.UserRequest;
import com.example.onlybunsbe.DTO.UserTokenState;
import com.example.onlybunsbe.exception.ResourceConflictException;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.service.UserService;
import com.example.onlybunsbe.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


//Kontroler zaduzen za autentifikaciju korisnika
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    // Prvi endpoint koji pogadja korisnik kada se loguje.
    // Tada zna samo svoje korisnicko ime i lozinku i to prosledjuje na backend.
    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletResponse response) {
        // Ukoliko kredencijali nisu ispravni, logovanje nece biti uspesno, desice se
        // AuthenticationException
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getEmail(), authenticationRequest.getPassword()));

        // Ukoliko je autentifikacija uspesna, ubaci korisnika u trenutni security
        // kontekst
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token za tog korisnika
        User user = (User) authentication.getPrincipal();
        System.out.println("User ID: " + user.getId());
        System.out.println("User Role: " + user.getRole().getName());
        String jwt = tokenUtils.generateToken(user.getEmail(), user.getId(), user.getRole().getName());
        int expiresIn = tokenUtils.getExpiredIn();

        // Vrati token kao odgovor na uspesnu autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user.getId()));
    }

    // Endpoint za registraciju novog korisnika
    @PostMapping("/signup")
    public ResponseEntity<User> addUser(@RequestBody UserRequest userRequest, UriComponentsBuilder ucBuilder) {
        User existUser = this.userService.findByUsername(userRequest.getUsername());

        if (existUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Username already exists");
        }

        User user = this.userService.save(userRequest);

        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    // Activation endpoint
    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("token") String token) {
        boolean isActivated = userService.activateUser(token);
        if (isActivated) {
            return ResponseEntity.ok("Account activated successfully. You can now log in.");
        } else {
            return ResponseEntity.badRequest().body("Invalid activation token.");
        }
    }
}