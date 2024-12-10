package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
    User findById(long id);
    User findByActivationToken(String activationToken);

    @Query("SELECT u FROM User u WHERE u.lastLoginDate < :threshold")
    List<User> findUsersWithLastLoginBefore(java.util.Date threshold);
}
