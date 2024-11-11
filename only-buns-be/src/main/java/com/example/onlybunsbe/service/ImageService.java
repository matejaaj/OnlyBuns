package com.example.onlybunsbe.service;

import com.example.onlybunsbe.model.Image;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
public class ImageService {
    private static final String IMAGE_DIRECTORY = "uploads/";
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 2MB u bajtovima

    public String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(IMAGE_DIRECTORY, fileName);

        Files.write(filePath, image.getBytes());

        return "/uploads/" + fileName;
    }

    public Image createImageEntity(MultipartFile image) throws IOException {
        validateImage(image);


        String imagePath = saveImage(image);


        Image imageEntity = new Image();
        imageEntity.setPath(imagePath);
        imageEntity.setCompressed(false);
        imageEntity.setUploadedAt(LocalDateTime.now());

        return imageEntity;
    }
    private void validateImage(MultipartFile image) {

        if (image.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        if (image.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("Use image less than 5MB.");
        }

        String contentType = image.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/png") &&
                        !contentType.equals("image/jpeg") &&
                        !contentType.equals("image/jpg"))) {
            throw new IllegalArgumentException("Invalid image format use some of the following formats: PNG, JPG, JPEG.");
        }
    }
}
