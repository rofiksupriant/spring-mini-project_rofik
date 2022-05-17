package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.dao.Table;
import com.rofik.miniproject.domain.dao.User;
import com.rofik.miniproject.domain.dto.request.LoginRequest;
import com.rofik.miniproject.repository.TableRepository;
import com.rofik.miniproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TableRepository tableRepository;
    @InjectMocks
    private AuthService authService;

    @Test
    void loginSuccess() {
        User user = User.builder().username("admin").password("$2a$12$8LCT9hwZwDh5NzqOC2epeeXRClqvQnzebrHgesb39B3Ex7.Ybov6q").build();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        LoginRequest loginRequest = LoginRequest.builder().username("admin").password("12345").build();
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());

    }

    @Test
    void loginTableSuccess() {
        Table table = Table.builder().uuid("random-uuid").number(1).build();
        when(tableRepository.findByUuid(anyString())).thenReturn(Optional.of(table));

        LoginRequest loginRequest = LoginRequest.builder().tableUuid("random-uuid").build();
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());

    }

    @Test
    void loginUserNotFound() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.ofNullable(null));

        LoginRequest loginRequest = LoginRequest.builder().username("admin").password("12345").build();
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
    }

    @Test
    void loginWrongPassword() {
        User user = User.builder().username("admin").password("$2a$12$8LCT9hwZwDh5NzqOC2epeeXRClqvQnzebrHgesb39B3Ex7.Ybov6q").build();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        LoginRequest loginRequest = LoginRequest.builder().username("admin").password("wrongpassword").build();
        ResponseEntity<Object> responseEntity = authService.login(loginRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
    }
}