package com.example.onlybunsbe.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(RestAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        // Log the exception with request details for better traceability
        logger.error("Authentication failed. Path: {}, Remote IP: {}, Error: {}",
                request.getRequestURI(), request.getRemoteAddr(), authException.getMessage());

        if (authException instanceof DisabledException) {
            // User account is not activated
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("{\"error\": \"Your account is not activated. Please check your email for the activation link.\"}");
        } else if (authException instanceof UsernameNotFoundException) {
            // Username not found
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\": \"User not found. Please check your credentials.\"}");
        } else if (authException instanceof BadCredentialsException) {
            // Incorrect password or email
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Invalid email or password. Please try again.\"}");
        } else {
            // Generic authentication error for unhandled exceptions
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Unauthorized: " + authException.getMessage() + "\"}");
        }

        // Ensure the response is flushed to the client
        response.getWriter().flush();
    }
}
