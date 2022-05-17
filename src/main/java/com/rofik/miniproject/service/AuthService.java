package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.UserRole;
import com.rofik.miniproject.domain.dao.Table;
import com.rofik.miniproject.domain.dao.User;
import com.rofik.miniproject.domain.dto.request.LoginRequest;
import com.rofik.miniproject.domain.dto.response.LoginResponse;
import com.rofik.miniproject.repository.TableRepository;
import com.rofik.miniproject.repository.UserRepository;
import com.rofik.miniproject.security.TokenProvider;
import com.rofik.miniproject.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
    private TableRepository tableRepository;
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

    public <T> T me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Optional<? extends GrantedAuthority> grantedAuthority = authentication.getAuthorities().stream().findFirst();
        T result;

        try {
            String authority = grantedAuthority.get().toString();
            if (authority.equals(UserRole.ADMIN.name()) || authority.equals(UserRole.EMPLOYEE.name())) {
                Optional<User> userOptional = userRepository.findByUsername(username);

                if (!userOptional.isPresent()) throw new AuthorizationServiceException("Unauthorized");
                return (T) userOptional.get();
            } else {
                Optional<Table> tableOptional = tableRepository.findByUuid(username);

                if (!tableOptional.isPresent()) throw new AuthorizationServiceException("Unauthorized");
                return (T) tableOptional.get();
            }
        } catch (AuthorizationServiceException exception) {
            log.error(exception.getLocalizedMessage());
            throw exception;
        }

    }
}
