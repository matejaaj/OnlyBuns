package com.example.onlybunsbe.repository;

import com.example.onlybunsbe.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i FROM Image i WHERE i.isCompressed = false AND i.uploadedAt < :cutoffDate")
    List<Image> findUncompressedImagesOlderThan(@Param("cutoffDate") LocalDateTime cutoffDate);
}
