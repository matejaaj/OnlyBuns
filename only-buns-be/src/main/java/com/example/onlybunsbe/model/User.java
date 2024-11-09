package com.example.onlybunsbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull
    @Size(min = 3, max = 100)
    @Column(name = "username", unique = true)
    private String username;

    @JsonIgnore
    @NotNull
    @Size(min = 8, max = 255)
    @Column(name = "password")
    private String password;

    @NotNull
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotNull
    @Size(max = 100)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(max = 100)
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "address")
    private String address;

    @Column(name = "enabled")
    private boolean enabled = false; // Podrazumevano false dok korisnik ne potvrdi email

    @JsonIgnore
    @Column(name = "last_password_reset_date")
    private Timestamp lastPasswordResetDate;

    @JsonIgnore
    @Column(name = "activation_token")
    private String activationToken; // Polje za aktivacioni token za email verifikaciju

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id") // Naziv kolone u User tabeli koja pokazuje na Role
    private Role role;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}