package com.example.onlybunsbe.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.onlybunsbe.DTO.UserDTO;
import com.example.onlybunsbe.model.User;
import com.example.onlybunsbe.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


// Primer kontrolera cijim metodama mogu pristupiti samo autorizovani korisnici
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
public class UserController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    // Za pristup ovoj metodi neophodno je da ulogovani korisnik ima ADMIN ulogu
    // Ukoliko nema, server ce vratiti gresku 403 Forbidden
    // Korisnik jeste autentifikovan, ali nije autorizovan da pristupi resursu
    @GetMapping("/user/{userId}")

    public User loadById(@PathVariable Long userId) {
        return this.userService.findById(userId);
    }

    @GetMapping("/user/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<User> loadAll() {
        return this.userService.findAll();
    }

    @GetMapping("/whoami")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public User user(Principal user) {
        return this.userService.findByUsername(user.getName());
    }

    // testna funkcija
    @GetMapping("/foo")
    public Map<String, String> getFoo() {
        Map<String, String> fooObj = new HashMap<>();
        fooObj.put("foo", "bar");
        return fooObj;
    }

    @GetMapping("/user/sort")
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(value = "sortBy", defaultValue = "email") String sortBy,
            @RequestParam(value = "isAscending", defaultValue = "true") boolean isAscending,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "minPosts", required = false) Integer minPosts,
            @RequestParam(value = "maxPosts", required = false) Integer maxPosts) {

        // Dodajemo logove za parametre
        System.out.println("sortBy: " + sortBy);
        System.out.println("isAscending: " + isAscending);
        System.out.println("name: " + name);
        System.out.println("email: " + email);
        System.out.println("minPosts: " + minPosts);
        System.out.println("maxPosts: " + maxPosts);

        List<UserDTO> users = userService.getAllUsers(name, email, minPosts, maxPosts, sortBy, isAscending);
        return ResponseEntity.ok(users);
    }

}
