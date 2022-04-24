package com.rofik.miniproject.util;

import com.rofik.miniproject.domain.common.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ResponseUtil {
    private ResponseUtil() {
    }

    public static <T> ResponseEntity<Object> build(String message, HttpStatus status) {
        return new ResponseEntity<>(
                ApiResponse.<T>builder()
                        .timestamp(LocalDateTime.now())
                        .message(message)
                        .status(status)
                        .build(),
                status
        );
    }

    public static <T> ResponseEntity<Object> build(String message, HttpStatus status, T data) {
        return new ResponseEntity<>(
                ApiResponse.<T>builder()
                        .timestamp(LocalDateTime.now())
                        .message(message)
                        .data(data)
                        .status(status)
                        .build(),
                status
        );
    }
}