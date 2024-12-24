package com.example.onlybunsbe.service;

import com.example.onlybunsbe.DTO.JwtAuthenticationRequest;
import com.example.onlybunsbe.DTO.UserRequest;
import com.example.onlybunsbe.DTO.UserTokenState;
import com.example.onlybunsbe.exception.ResourceConflictException;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthService {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private AttemptService attemptService;



    public ResponseEntity<?> login(JwtAuthenticationRequest authenticationRequest, HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();

        // Check if the IP address is blocked
        if (attemptService.isBlocked(ipAddress)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body("Too many login attempts. Please try again later.");
        }

        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(), authenticationRequest.getPassword())
            );

            // Reset failed login attempts
            attemptService.clearAttempts(ipAddress);

            // Set security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT token
            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user.getEmail(), user.getId(), user.getRole().getName());
            int expiresIn = tokenUtils.getExpiredIn();



            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, user.getId()));

        } catch (BadCredentialsException ex) {
            // Record a failed attempt
            attemptService.recordAttempt(ipAddress);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Invalid email or password. Please try again.");
        }
    }

    public User register(UserRequest userRequest) {
        User existingUser = userService.findByUsername(userRequest.getUsername());
        if (existingUser != null) {
            throw new ResourceConflictException(userRequest.getId(), "Username already exists");
        }
        return userService.save(userRequest);
    }

    public boolean activateUser(String token) {
        return userService.activateUser(token);
    }
}
