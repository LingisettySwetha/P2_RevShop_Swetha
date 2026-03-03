package com.rev.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get("uploads").toAbsolutePath().normalize();
        Path imageDir = uploadDir.resolve("images").normalize();

        try {
            Files.createDirectories(imageDir);
        } catch (IOException ignored) {
        }

        String uploadLocation = "file:" + uploadDir.toString().replace('\\', '/') + "/";
        String imageLocation = "file:" + imageDir.toString().replace('\\', '/') + "/";

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadLocation);

        registry.addResourceHandler("/images/**")
                .addResourceLocations(
                        imageLocation,
                        "classpath:/static/images/");
    }
}
