package com.example.onlybunsbe.repository;

import java.util.Optional;

import com.example.onlybunsbe.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
