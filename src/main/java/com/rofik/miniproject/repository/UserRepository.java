package com.rofik.miniproject.repository;

import com.rofik.miniproject.domain.dao.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsernameAndIdNot(String username, Long id);

    Optional<User> findByEmailAndIdNot(String email, Long id);
}