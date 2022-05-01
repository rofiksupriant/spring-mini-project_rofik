package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.dto.request.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Test
    void loginSuccess() {
        LoginRequest loginRequest = new LoginRequest("admin", "12345");
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());

    }

    @Test
    void loginUserNotFound() {
        LoginRequest loginRequest = new LoginRequest("notfound", "12345");
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
    }

    @Test
    void loginWrongPassword() {
        LoginRequest loginRequest = new LoginRequest("admin", "wrong");
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
    }
}