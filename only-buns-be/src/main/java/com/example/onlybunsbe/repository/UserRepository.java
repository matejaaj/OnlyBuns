package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
    User findByEmail(String email);
    User findById(long id);
    User findByActivationToken(String activationToken);

    @Query("SELECT u FROM User u LEFT JOIN FETCH u.posts WHERE u.lastLoginDate < :cutoffDate")
    List<User> findUsersWithLastLoginBefore(@Param("cutoffDate") Instant cutoffDate);


}
