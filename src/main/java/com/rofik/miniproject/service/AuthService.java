package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.dto.request.LoginRequest;
import com.rofik.miniproject.domain.dto.response.LoginResponse;
import com.rofik.miniproject.repository.UserRepository;
import com.rofik.miniproject.security.TokenProvider;
import com.rofik.miniproject.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthService {
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private CustomUserDetailService userDetailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ResponseEntity<Object> login(LoginRequest loginRequest) {
        try {
            var username = "";
            var password = "";
            if (loginRequest.getTableUuid() != null) {
                username = loginRequest.getTableUuid();
            } else {
                username = loginRequest.getUsername();
                password = loginRequest.getPassword();
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            }

            UserDetails userDetails = userDetailService.loadUserByUsername(username);
            String token = tokenProvider.generateToken(userDetails);

            LoginResponse response = LoginResponse.builder()
                    .tokenType("Bearer")
                    .token(token)
                    .username(userDetails.getUsername())
                    .role(userDetails.getAuthorities().stream().findFirst().get().toString())
                    .build();

            return ResponseUtil.build("Login successfully", HttpStatus.OK, response);
        } catch (UsernameNotFoundException exception) {
            log.error("Failed load user by username with error: {}", exception.getLocalizedMessage());
            return ResponseUtil.build("User not found", HttpStatus.NOT_FOUND);
        } catch (BadCredentialsException exception) {
            log.error("Failed to authenticate: {}", exception.getLocalizedMessage());
            return ResponseUtil.build("Wrong password", HttpStatus.BAD_REQUEST);
        }
    }
}
