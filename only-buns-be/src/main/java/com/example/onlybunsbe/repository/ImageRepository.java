package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
