package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.common.UserRole;
import com.rofik.miniproject.domain.dao.Table;
import com.rofik.miniproject.domain.dao.User;
import com.rofik.miniproject.domain.dto.request.LoginRequest;
import com.rofik.miniproject.repository.TableRepository;
import com.rofik.miniproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TableRepository tableRepository;
    @Autowired
    private AuthService authService;

    @Test
    void loginSuccess() {
        User user = User.builder().username("rofik").password("$2a$12$8LCT9hwZwDh5NzqOC2epeeXRClqvQnzebrHgesb39B3Ex7.Ybov6q").role(UserRole.ADMIN).build();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        LoginRequest loginRequest = LoginRequest.builder().username("rofik").password("12345").build();
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());

    }

    @Test
    void loginTableSuccess() {
        Table table = Table.builder().id(1L).uuid("87560ffd-8336-4397-ba70-79d3d332d91c").number(1).build();
        when(tableRepository.findByUuid(any())).thenReturn(Optional.of(table));

        LoginRequest loginRequest = LoginRequest.builder().tableUuid("87560ffd-8336-4397-ba70-79d3d332d91c").build();
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());

    }

    @Test
    void loginUserNotFound() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(null));

        LoginRequest loginRequest = LoginRequest.builder().username("any").password("12345").build();
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
    }

    @Test
    void loginWrongPassword() {
        User user = User.builder().username("admin").password("$2a$12$8LCT9hwZwDh5NzqOC2epeeXRClqvQnzebrHgesb39B3Ex7.Ybov6q").role(UserRole.ADMIN).build();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        LoginRequest loginRequest = LoginRequest.builder().username("admin").password("wrongpassword").build();
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
    }
}