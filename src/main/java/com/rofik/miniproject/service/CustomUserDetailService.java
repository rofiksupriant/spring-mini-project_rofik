package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.UserRole;
import com.rofik.miniproject.domain.dao.Table;
import com.rofik.miniproject.domain.dao.User;
import com.rofik.miniproject.repository.TableRepository;
import com.rofik.miniproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TableRepository tableRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = "";
        String role = "";

        if (username.length() == 36) {
            Table table = tableRepository.findByUuid(username).orElse(null);
            if (table == null) throw new UsernameNotFoundException(String.format("Table %s not found", username));

            role = UserRole.CUSTOMER.name();
        } else {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) throw new UsernameNotFoundException(String.format("User %s not found", username));

            password = user.getPassword();
            role = user.getRole().name();
        }

        return new org.springframework.security.core.userdetails.User(
                username,
                password,
                Collections.singletonList(new SimpleGrantedAuthority(role))
        );
    }
}
