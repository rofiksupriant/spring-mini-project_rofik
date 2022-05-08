package com.rofik.miniproject.repository;

import com.rofik.miniproject.domain.dao.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByDeleted(boolean b);
}