package com.example.onlybunsbe.Interceptor;

import com.example.onlybunsbe.service.UserService;
import com.example.onlybunsbe.util.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.URI;
import java.util.Map;

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private final TokenUtils tokenUtils;
    private final UserService userService; // Promeni u final

    @Autowired
    public JwtHandshakeInterceptor(TokenUtils tokenUtils, UserService userService) {
        this.tokenUtils = tokenUtils;
        this.userService = userService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        try {
            URI uri = request.getURI();

            String query = uri.getQuery();
            String jwtToken = null;

            if (query != null) {
                for (String param : query.split("&")) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && "token".equals(keyValue[0])) {
                        jwtToken = keyValue[1];
                        break;
                    }
                }
            }

            if (jwtToken == null) {
                System.out.println("JWT token not found in query params");
                return false;
            }

            System.out.println("JWT token: " + jwtToken);

            String email = tokenUtils.getEmailFromToken(jwtToken);
            if (email == null) {
                System.out.println("Email could not be extracted from token");
                return false;
            }

            UserDetails userDetails = userService.findByEmail(email);
            if (!tokenUtils.validateToken(jwtToken, userDetails)) {
                System.out.println("Token validation failed for email: " + email);
                return false;
            }

            attributes.put("username", email);
            return true;

        } catch (Exception e) {
            System.err.println("Error in handshake: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Nije potrebno implementirati
    }
}
