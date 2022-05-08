package com.rofik.miniproject.repository;

import com.rofik.miniproject.domain.dao.Category;
import com.rofik.miniproject.domain.dao.Product;
import com.rofik.miniproject.domain.dao.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
    Optional<ProductCategory> findByProductAndCategory(Product product, Category category);

    Optional<ProductCategory> findByProductAndCategoryAndIdNot(Product product, Category category, Long id);
}