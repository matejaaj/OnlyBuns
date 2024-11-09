package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

