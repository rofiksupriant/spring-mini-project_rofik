package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.dao.User;
import com.rofik.miniproject.domain.dto.request.UserRequest;
import com.rofik.miniproject.domain.dto.response.UserResponse;
import com.rofik.miniproject.repository.UserRepository;
import com.rofik.miniproject.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.*;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> createOne(UserRequest request) {
        try {
            Optional<User> userOptionalByUsername = userRepository.findByUsername(request.getUsername());
            if (userOptionalByUsername.isPresent())
                return ResponseUtil.build(USER_USERNAME_EXIST, HttpStatus.BAD_REQUEST);

            if (request.getEmail() != null) {
                Optional<User> userOptionalByEmail = userRepository.findByEmail(request.getEmail());
                if (userOptionalByEmail.isPresent())
                    return ResponseUtil.build(USER_EMAIL_EXIST, HttpStatus.BAD_REQUEST);
            }

            User user = new User();
            BeanUtils.copyProperties(request, user);
            user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
            user = userRepository.saveAndFlush(user);

            UserResponse response = new UserResponse();
            BeanUtils.copyProperties(user, response);
            return ResponseUtil.build(USER_CREATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error create new user: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getAll() {
        try {
            log.info("Get all user");
            List<User> userList = userRepository.findAll();

            List<UserResponse> result = new ArrayList<>();
            userList.forEach(user -> result.add(
                    UserResponse.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .role(user.getRole())
                            .build()
            ));

            return ResponseUtil.build(USER_GET_ALL, HttpStatus.OK, result);
        } catch (Exception e) {
            log.error("Error get all user: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getOneById(Long id) {
        try {
            Optional<User> userOptional = userRepository.findById(id);

            if (userOptional.isEmpty()) return ResponseUtil.build(USER_NOT_FOUND, HttpStatus.NOT_FOUND);

            User user = userOptional.get();
            UserResponse response = new UserResponse();
            BeanUtils.copyProperties(user, response);

            return ResponseUtil.build(USER_GET_BY_ID, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error get user by id: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> updateOne(Long id, UserRequest request) {
        try {
            log.info("Update user {}", id);

            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) return ResponseUtil.build(USER_NOT_FOUND, HttpStatus.NOT_FOUND);

            Optional<User> userOptionalByUsername = userRepository.findByUsernameAndIdNot(request.getUsername(), id);
            if (userOptionalByUsername.isPresent())
                return ResponseUtil.build(USER_USERNAME_EXIST, HttpStatus.BAD_REQUEST);

            if (request.getEmail() != null) {
                Optional<User> userOptionalByEmail = userRepository.findByEmailAndIdNot(request.getEmail(), id);
                if (userOptionalByEmail.isPresent())
                    return ResponseUtil.build(USER_EMAIL_EXIST, HttpStatus.BAD_REQUEST);
            }

            User user = User.builder()
                    .id(userOptional.get().getId())
                    .email(request.getEmail())
                    .role(request.getRole())
                    .active(request.getActive())
                    .name(request.getName())
                    .username(request.getUsername())
                    .build();
            if (request.getPassword() != null) {
                user.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
            }

            user = userRepository.saveAndFlush(user);

            UserResponse response = new UserResponse();
            BeanUtils.copyProperties(user, response);
            return ResponseUtil.build(USER_UPDATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error update user with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> deleteOne(Long id) {
        try {
            log.info("Delete user with id {}", id);

            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isEmpty()) return ResponseUtil.build(USER_NOT_FOUND, HttpStatus.NOT_FOUND);

            userRepository.deleteById(id);
            return ResponseUtil.build(USER_DELETED, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error delete user with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }

    }
}
