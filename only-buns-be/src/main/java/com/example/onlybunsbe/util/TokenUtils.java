package com.example.onlybunsbe.util;

import java.util.Date;

import com.example.onlybunsbe.model.User;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Utility klasa za rad sa JSON Web Tokenima
@Component
public class TokenUtils {

    // Izdavac tokena
    @Value("spring-security-example")
    private String APP_NAME;

    // Tajna koju samo backend aplikacija treba da zna kako bi mogla da generise i proveri JWT https://jwt.io/
    @Value("somesecret")
    public String SECRET;

    // Period vazenja tokena - 30 minuta
    @Value("1800000")
    private int EXPIRES_IN;

    // Naziv headera kroz koji ce se prosledjivati JWT u komunikaciji server-klijent
    @Value("Authorization")
    private String AUTH_HEADER;

    private static final String AUDIENCE_WEB = "web";
    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

    // ============= Funkcije za generisanje JWT tokena =============

    /**
     * Funkcija za generisanje JWT tokena.
     *
     * @param email Korisničko ime korisnika kojem se token izdaje
     * @return JWT token
     */
    public String generateToken(String email, Long userId, String role) {
        System.out.println("Generating token with email: " + email + ", userId: " + userId + ", role: " + role);

        return Jwts.builder()
                .setIssuer(APP_NAME)
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setAudience(generateAudience())
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith(SIGNATURE_ALGORITHM, SECRET)
                .compact();
    }

    private String generateAudience() {
        return AUDIENCE_WEB;
    }

    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + EXPIRES_IN);
    }

    // =================================================================

    // ============= Funkcije za citanje informacija iz JWT tokena =============

    public String getToken(HttpServletRequest request) {
        String authHeader = getAuthHeaderFromHeader(request);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public String getEmailFromToken(String token) {
        String email;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            email = claims.getSubject();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            email = null;
        }
        return email;
    }

    public Long getUserIdFromToken(String token) {
        Long userId;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            System.out.println("Claims extracted from token (in getUserIdFromToken): " + claims); // Ispis claims pre izvlačenja userId
            userId = claims.get("userId", Integer.class) != null ? Long.valueOf(claims.get("userId", Integer.class)) : null;


            if (userId == null) {
                System.out.println("Warning: userId extracted as null from claims!"); // Ispis ako je userId null
            } else {
                System.out.println("Extracted userId from claims: " + userId); // Ispis ako userId nije null
            }

        } catch (ExpiredJwtException ex) {
            System.out.println("Token expired: " + ex.getMessage());
            throw ex;
        } catch (Exception e) {
            System.out.println("Exception when extracting userId: " + e.getMessage());
            userId = null;
        }
        return userId;
    }

    public String getRoleFromToken(String token) {
        String role;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            role = claims.get("role", String.class);
            System.out.println("Extracted role from claims: " + role);
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            role = null;
        }
        return role;
    }

    // ============= Pomocne funkcije za rad sa JWT tokenima =============

    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("Claims extracted from token: " + claims);
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        User user = (User) userDetails;
        final String email = getEmailFromToken(token);
        final Date created = getIssuedAtDateFromToken(token);
        return (email != null
                && email.equals(user.getEmail())
                && !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate()));
    }

    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    private Date getIssuedAtDateFromToken(String token) {
        Date issueAt;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            issueAt = claims.getIssuedAt();
        } catch (ExpiredJwtException ex) {
            throw ex;
        } catch (Exception e) {
            issueAt = null;
        }
        return issueAt;
    }

    public int getExpiredIn() {
        return EXPIRES_IN;
    }

    public String getAuthHeaderFromHeader(HttpServletRequest request) {
        return request.getHeader(AUTH_HEADER);
    }

}
