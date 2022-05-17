package com.rofik.miniproject.util;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
    private FileUploadUtil() {
    }

    private static Path basePath = Paths.get("/kampus-merdeka/mini-project/files").toAbsolutePath();

    public static String saveFile(String directory, String fileName, MultipartFile multipartFile)
            throws IOException {

        if (!Files.exists(basePath.resolve(directory))) {
            Files.createDirectories(basePath.resolve(directory));
        }

        String fileCode = RandomStringUtils.randomAlphanumeric(8);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = Paths.get(directory).resolve(fileCode + "-" + fileName);
            Path fullFilePath = basePath.resolve(filePath);
            Files.copy(inputStream, fullFilePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath.toUri().toString();
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }
    }
}