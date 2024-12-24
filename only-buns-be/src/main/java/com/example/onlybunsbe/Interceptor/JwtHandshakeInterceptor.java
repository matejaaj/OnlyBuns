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
            // Ekstrakcija URL-a iz zahteva
            URI uri = request.getURI();
            System.out.println("Request URI: " + uri);

            // Parsiranje query parametara kako bi se ekstrahovao token
            String query = uri.getQuery(); // Dobićete npr. "token=eyJhbGciOi..."
            String jwtToken = null;

            if (query != null) {
                for (String param : query.split("&")) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2 && "token".equals(keyValue[0])) {
                        jwtToken = keyValue[1]; // Ekstrakcija vrednosti tokena
                        break;
                    }
                }
            }

            if (jwtToken != null) {
                System.out.println("Extracted Token: " + jwtToken);

                // Ekstrakcija email-a iz tokena
                String email = tokenUtils.getEmailFromToken(jwtToken);
                System.out.println("Extracted Email: " + email);

                if (email != null) {
                    // Dohvatanje korisnika iz baze na osnovu email-a
                    UserDetails userDetails = userService.findByEmail(email);

                    // Validacija tokena sa informacijama korisnika
                    if (tokenUtils.validateToken(jwtToken, userDetails)) {
                        // Dodavanje korisničkog imena u atribute za dalje korišćenje
                        attributes.put("username", email);
                        System.out.println("Handshake successful for user: " + email);
                        return true;
                    } else {
                        System.out.println("Token validation failed for user: " + email);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error during token validation: " + e.getMessage());
        }

        System.out.println("Token invalid or missing");
        return false; // Odbijanje handshake-a ukoliko validacija ne uspe
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // Nije potrebno implementirati
    }
}
