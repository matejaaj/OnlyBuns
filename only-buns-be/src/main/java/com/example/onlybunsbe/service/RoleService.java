package com.example.onlybunsbe.service;

import java.util.List;
import java.util.Optional;

import com.example.onlybunsbe.model.Role;
import com.example.onlybunsbe.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findById(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    public Optional<Role> findByName(String name) {
        return this.roleRepository.findByName(name);
    }

    public List<Role> findAll() {
        return this.roleRepository.findAll();
    }
}