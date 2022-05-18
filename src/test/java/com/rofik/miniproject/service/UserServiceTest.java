package com.rofik.miniproject.service;

import com.rofik.miniproject.constant.ResponseContant;
import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.common.UserRole;
import com.rofik.miniproject.domain.dao.User;
import com.rofik.miniproject.domain.dto.request.UserRequest;
import com.rofik.miniproject.domain.dto.response.UserResponse;
import com.rofik.miniproject.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Test
    void createOneSuccess() {
        User user = User.builder().id(1L).name("Full Name").username("username").email("username@mail.com").role(UserRole.EMPLOYEE).build();
        when(userRepository.saveAndFlush(any())).thenReturn(user);
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(null));
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(null));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("admin@mail.com");
        userRequest.setPassword("12345");

        ResponseEntity responseEntity = userService.createOne(userRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        UserResponse data = (UserResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(ResponseContant.USER_CREATED, apiResponse.getMessage());
        assertEquals("username", data.getUsername());
    }

    @Test
    void createOneUsernameExist() {
        User user = User.builder().id(1L).name("Full Name").username("username").email("username@mail.com").role(UserRole.EMPLOYEE).build();
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("admin@mail.com");

        ResponseEntity responseEntity = userService.createOne(userRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
        assertEquals(USER_USERNAME_EXIST, apiResponse.getMessage());
    }

    @Test
    void createOneEmailExist() {
        User user = User.builder().id(1L).name("Full Name").username("username").email("username@mail.com").role(UserRole.EMPLOYEE).build();
        when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(null));
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(user));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("admin@mail.com");

        ResponseEntity responseEntity = userService.createOne(userRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
        assertEquals(USER_EMAIL_EXIST, apiResponse.getMessage());
    }

    @Test
    void getAll() {
        List<User> userList = Arrays.asList(
                User.builder().id(1L).name("Full Name").username("username1").email("username1@mail.com").role(UserRole.EMPLOYEE).build(),
                User.builder().id(2L).name("Full Name").username("username2").email("username2@mail.com").role(UserRole.EMPLOYEE).build(),
                User.builder().id(3L).name("Full Name").username("username3").email("username3@mail.com").role(UserRole.EMPLOYEE).build(),
                User.builder().id(4L).name("Full Name").username("username4").email("username4@mail.com").role(UserRole.EMPLOYEE).build(),
                User.builder().id(5L).name("Full Name").username("username5").email("username5@mail.com").role(UserRole.EMPLOYEE).build()
        );
        when(userRepository.findAll()).thenReturn(userList);

        ResponseEntity responseEntity = userService.getAll();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        List<UserResponse> data = (List<UserResponse>) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(USER_GET_ALL, apiResponse.getMessage());
        assertEquals(5, data.size());
    }

    @Test
    void getOneByIdSuccess() {
        User user = User.builder().id(1L).name("Full Name").username("username").email("username@mail.com").role(UserRole.EMPLOYEE).build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        ResponseEntity responseEntity = userService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        UserResponse data = (UserResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(USER_GET_BY_ID, apiResponse.getMessage());
        assertEquals(1L, data.getId());
    }

    @Test
    void getOneByIdNotFound() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = userService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(USER_NOT_FOUND, apiResponse.getMessage());
    }

    @Test
    void updateOneSuccess() {
        User user = User.builder().id(1L).name("Full Name").username("username").email("username@mail.com").role(UserRole.EMPLOYEE).build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameAndIdNot(any(), any())).thenReturn(Optional.ofNullable(null));
        when(userRepository.findByEmailAndIdNot(any(), any())).thenReturn(Optional.ofNullable(null));
        when(userRepository.saveAndFlush(any())).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("admin@mail.com");

        ResponseEntity responseEntity = userService.updateOne(1L, userRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        UserResponse data = (UserResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(USER_UPDATED, apiResponse.getMessage());
        assertEquals(1L, data.getId());
    }

    @Test
    void updateOneWithPasswordSuccess() {
        User user = User.builder().id(1L).name("Full Name").username("username").email("username@mail.com").role(UserRole.EMPLOYEE).build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameAndIdNot(any(), any())).thenReturn(Optional.ofNullable(null));
        when(userRepository.findByEmailAndIdNot(any(), any())).thenReturn(Optional.ofNullable(null));
        when(userRepository.saveAndFlush(any())).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("admin@mail.com");
        userRequest.setPassword("12345");

        ResponseEntity responseEntity = userService.updateOne(1L, userRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        UserResponse data = (UserResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(USER_UPDATED, apiResponse.getMessage());
        assertEquals(1L, data.getId());
    }

    @Test
    void updateOneNotFound() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("admin@mail.com");

        ResponseEntity responseEntity = userService.updateOne(1L, userRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(USER_NOT_FOUND, apiResponse.getMessage());
    }

    @Test
    void updateOneUsernameExist() {
        User user = User.builder().id(1L).name("Full Name").username("username").email("username@mail.com").role(UserRole.EMPLOYEE).build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameAndIdNot(any(), any())).thenReturn(Optional.ofNullable(user));
        when(userRepository.saveAndFlush(any())).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("admin@mail.com");

        ResponseEntity responseEntity = userService.updateOne(1L, userRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
        assertEquals(USER_USERNAME_EXIST, apiResponse.getMessage());
    }

    @Test
    void updateOneEmailExist() {
        User user = User.builder().id(1L).name("Full Name").username("username").email("username@mail.com").role(UserRole.EMPLOYEE).build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));
        when(userRepository.findByUsernameAndIdNot(any(), any())).thenReturn(Optional.ofNullable(null));
        when(userRepository.findByEmailAndIdNot(any(), any())).thenReturn(Optional.ofNullable(user));
        when(userRepository.saveAndFlush(any())).thenReturn(user);

        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("username");
        userRequest.setEmail("admin@mail.com");

        ResponseEntity responseEntity = userService.updateOne(1L, userRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
        assertEquals(USER_EMAIL_EXIST, apiResponse.getMessage());
    }

    @Test
    void deleteOneSuccess() {
        User user = User.builder().id(1L).name("Full Name").username("username").email("username@mail.com").role(UserRole.EMPLOYEE).build();
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        ResponseEntity responseEntity = userService.deleteOne(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(USER_DELETED, apiResponse.getMessage());
    }

    @Test
    void deleteOneNotFound() {
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = userService.deleteOne(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(USER_NOT_FOUND, apiResponse.getMessage());
    }
}