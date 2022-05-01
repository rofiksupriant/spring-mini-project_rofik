package com.rofik.miniproject.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;
    private String token;
    private UserDetails userDetails;

    @BeforeEach
    void setup() {
        this.userDetails = new User("admin", "12345", Collections.singletonList(new SimpleGrantedAuthority("ADMIN")));
        String token = tokenProvider.generateToken(this.userDetails);
        this.token = token;
    }

    @Test
    void generateToken() {
        assertInstanceOf(String.class, this.token);
    }

    @Test
    void getUsernameFromToken() {
        assertInstanceOf(String.class, tokenProvider.getUsernameFromToken(this.token));
        assertEquals("admin", tokenProvider.getUsernameFromToken(this.token));
    }

    @Test
    void getExpirationDateFromToken() {
        assertInstanceOf(Date.class, tokenProvider.getExpirationDateFromToken(this.token));
    }

    @Test
    void validateTokenTrue() {
        assertTrue(tokenProvider.validateToken(token, this.userDetails));
    }

    @Test
    void validateTokenFalse() {
        assertFalse(tokenProvider.validateToken(token, new User(
                        "other username",
                        this.userDetails.getUsername(),
                        this.userDetails.getAuthorities())
                )
        );
    }
}