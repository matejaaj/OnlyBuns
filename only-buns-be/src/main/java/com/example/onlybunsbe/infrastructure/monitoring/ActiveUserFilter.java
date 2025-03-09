package com.example.onlybunsbe.infrastructure.monitoring;

import com.example.onlybunsbe.infrastructure.monitoring.ActiveUserTracker;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class ActiveUserFilter extends OncePerRequestFilter {

    private final ActiveUserTracker activeUserTracker;

    public ActiveUserFilter(ActiveUserTracker activeUserTracker) {
        this.activeUserTracker = activeUserTracker;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        activeUserTracker.trackUser(request);


        filterChain.doFilter(request, response);
    }
}