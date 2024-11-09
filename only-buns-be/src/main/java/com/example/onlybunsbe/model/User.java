package com.example.onlybunsbe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="users")
public class User implements UserDetails {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
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

    // Getteri i setteri

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        this.setLastPasswordResetDate(new Timestamp(new Date().getTime()));
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role); // Pretvara ulogu u listu sa jednim elementom
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