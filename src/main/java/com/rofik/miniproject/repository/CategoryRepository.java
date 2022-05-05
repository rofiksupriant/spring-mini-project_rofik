package com.rofik.miniproject.repository;

import com.rofik.miniproject.domain.dao.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    Optional<Category> findByNameAndIdNot(String name, Long id);
}