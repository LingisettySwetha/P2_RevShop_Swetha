package com.rev.app.service;

import com.rev.app.exception.InvalidRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageStorageServiceImpl implements IImageStorageService {

    private static final Path PRODUCT_UPLOAD_DIR = Paths.get("uploads", "products");

    @Override
    public String storeProductImage(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new InvalidRequestException("Only image files are allowed");
        }

        String original = file.getOriginalFilename();
        String ext = ".jpg";
        if (original != null) {
            int idx = original.lastIndexOf('.');
            if (idx >= 0 && idx < original.length() - 1) {
                ext = original.substring(idx).toLowerCase();
            }
        }

        String filename = UUID.randomUUID() + ext;

        try {
            Files.createDirectories(PRODUCT_UPLOAD_DIR);
            Path target = PRODUCT_UPLOAD_DIR.resolve(filename);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/products/" + filename;
        } catch (IOException ex) {
            throw new InvalidRequestException("Failed to store image");
        }
    }
}
