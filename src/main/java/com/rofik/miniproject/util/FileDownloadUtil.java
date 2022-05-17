package com.rofik.miniproject.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileDownloadUtil {
    private static Path basePath = Paths.get("/kampus-merdeka/mini-project/files").toAbsolutePath();

    public static Resource getFileAsResource(String filePath) throws IOException {
        Path path = basePath.resolve(filePath);

        Resource resource = new UrlResource(path.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new IOException("File not found " + filePath);
        }
    }
}