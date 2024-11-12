package com.example.onlybunsbe.service;

import com.example.onlybunsbe.model.Image;
import com.example.onlybunsbe.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImageService {
    private static final String IMAGE_DIRECTORY = "uploads/"; // Relativna putanja za vraćanje putanje ka slici
    private static final String ABSOLUTE_IMAGE_DIRECTORY = Paths.get("").toAbsolutePath().toString() + "/uploads/"; // Apsolutna putanja za operacije sa fajlovima
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB u bajtovima

    @Autowired
    private ImageRepository imageRepository;

    public String saveImage(MultipartFile image) throws IOException {
        if (image.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        File directory = new File(ABSOLUTE_IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = Paths.get(ABSOLUTE_IMAGE_DIRECTORY, fileName);

        Files.write(filePath, image.getBytes());

        return fileName; // Vraća samo ime fajla
    }

    public Image createImageEntity(MultipartFile image) throws IOException {
        validateImage(image);

        String imagePath = saveImage(image);

        Image imageEntity = new Image();
        imageEntity.setPath(imagePath); // Čuvamo samo ime fajla
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
            throw new IllegalArgumentException("Invalid image format. Use one of the following formats: PNG, JPG, JPEG.");
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void scheduledImageCompression() {
        compressOldImages();
    }

    public void compressOldImages() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusMonths(1);
        List<Image> oldUncompressedImages = imageRepository.findUncompressedImagesOlderThan(cutoffDate);

        for (Image image : oldUncompressedImages) {
            try {
                String compressedImagePath = compressImage(image.getPath());

                // Ažuriraj path i status u bazi da je slika kompresovana
                image.setPath(compressedImagePath);
                image.setCompressed(true);
                imageRepository.save(image); // Sačuvaj izmene u bazi

            } catch (IOException e) {
                System.err.println("Greška prilikom kompresije slike: " + image.getPath());
                e.printStackTrace();
            }
        }
    }

    private String compressImage(String fileName) throws IOException {
        // Prikazujemo ime fajla koji pokušavamo da kompresujemo
        System.out.println("Starting compression for file: " + fileName);

        // Kreiramo apsolutnu putanju za originalnu sliku
        File inputFile = new File(ABSOLUTE_IMAGE_DIRECTORY + fileName);
        System.out.println("Input file path: " + inputFile.getAbsolutePath());

        // Provera da li fajl postoji
        if (!inputFile.exists()) {
            System.err.println("Input file not found: " + inputFile.getAbsolutePath());
            throw new IOException("Input file not found: " + inputFile.getAbsolutePath());
        }

        BufferedImage image = ImageIO.read(inputFile);
        if (image == null) {
            System.err.println("Could not read the input file as an image: " + inputFile.getAbsolutePath());
            throw new IOException("Could not read the input file as an image: " + inputFile.getAbsolutePath());
        }
        System.out.println("Image read successfully: " + inputFile.getAbsolutePath());

        // Kreiranje novog fajla u istom direktorijumu sa prefiksom "compressed_"
        String compressedFileName = "compressed_" + fileName;
        File compressedFile = new File(ABSOLUTE_IMAGE_DIRECTORY + compressedFileName);
        System.out.println("Compressed file will be saved as: " + compressedFile.getAbsolutePath());

        // Provera i kreiranje direktorijuma ako ne postoji
        File directory = new File(ABSOLUTE_IMAGE_DIRECTORY);
        if (!directory.exists()) {
            System.out.println("Directory does not exist. Creating directory: " + directory.getAbsolutePath());
            directory.mkdirs();
        }

        String formatName = "jpg"; // default format
        if (fileName.endsWith(".png")) {
            formatName = "png";
        } else if (fileName.endsWith(".jpeg")) {
            formatName = "jpeg";
        }
// Onda koristite `formatName` prilikom kompresije:
        boolean isWritten = ImageIO.write(image, formatName, compressedFile);
        if (!isWritten) {
            System.err.println("Failed to write compressed image: " + compressedFile.getAbsolutePath());
            throw new IOException("Failed to write compressed image: " + compressedFile.getAbsolutePath());
        }

        System.out.println("Compression completed successfully for file: " + compressedFileName);

        // Vraća samo ime fajla s prefiksom "compressed_"
        return compressedFileName;
    }
}

