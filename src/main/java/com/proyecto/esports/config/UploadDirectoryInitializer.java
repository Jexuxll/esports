package com.proyecto.esports.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class UploadDirectoryInitializer implements ApplicationRunner {

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Path targetDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(targetDir);

        Path projectImagesDir = Paths.get(System.getProperty("user.dir"), "Imagenes").toAbsolutePath().normalize();
        if (Files.isDirectory(projectImagesDir) && !projectImagesDir.equals(targetDir)) {
            copyMissingFiles(projectImagesDir, targetDir);
        }
    }

    private void copyMissingFiles(Path sourceDir, Path targetDir) throws IOException {
        try (Stream<Path> files = Files.list(sourceDir)) {
            files.filter(Files::isRegularFile)
                 .forEach(sourceFile -> {
                     Path targetFile = targetDir.resolve(sourceFile.getFileName().toString());
                     if (!Files.exists(targetFile)) {
                         try {
                             Files.copy(sourceFile, targetFile, StandardCopyOption.COPY_ATTRIBUTES);
                         } catch (IOException ignored) {
                             // Skip problematic files and continue startup.
                         }
                     }
                 });
        }
    }
}
