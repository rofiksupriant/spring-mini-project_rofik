package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.dao.Category;
import com.rofik.miniproject.domain.dao.Product;
import com.rofik.miniproject.domain.dao.ProductCategory;
import com.rofik.miniproject.domain.dto.request.ProductCategoryRequest;
import com.rofik.miniproject.domain.dto.response.CategoryResponse;
import com.rofik.miniproject.domain.dto.response.ProductCategoryResponse;
import com.rofik.miniproject.domain.dto.response.ProductResponse;
import com.rofik.miniproject.repository.CategoryRepository;
import com.rofik.miniproject.repository.ProductCategoryRepository;
import com.rofik.miniproject.repository.ProductRepository;
import com.rofik.miniproject.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.*;

@Slf4j
@Service
public class ProductCategoryService {
    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    public ResponseEntity<Object> createOne(ProductCategoryRequest request) {
        try {
            Optional<Product> productOptional = productRepository.findById(request.getProductId());
            if (productOptional.isEmpty()) return ResponseUtil.build(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);

            Optional<Category> categoryOptional = categoryRepository.findById(request.getCategoryId());
            if (categoryOptional.isEmpty()) return ResponseUtil.build(CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);

            Product product = productOptional.get();
            Category category = categoryOptional.get();

            Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findByProductAndCategory(product, category);
            if (productCategoryOptional.isPresent())
                return ResponseUtil.build(PRODUCT_CATEGORY_EXIST, HttpStatus.BAD_REQUEST);

            ProductCategory productCategory = new ProductCategory();
            productCategory.setProduct(product);
            productCategory.setCategory(category);

            productCategory = productCategoryRepository.saveAndFlush(productCategory);

            ProductResponse productResponse = new ProductResponse();
            BeanUtils.copyProperties(productCategory.getProduct(), productResponse);

            CategoryResponse categoryResponse = new CategoryResponse();
            BeanUtils.copyProperties(productCategory.getCategory(), categoryResponse);

            ProductCategoryResponse response = ProductCategoryResponse.builder()
                    .id(productCategory.getId())
                    .product(productResponse)
                    .category(categoryResponse)
                    .build();

            return ResponseUtil.build(PRODUCT_CATEGORY_CREATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error create new product category: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getAll() {
        try {
            log.info("Get all productCategory");
            List<ProductCategory> productCategoryList = productCategoryRepository.findAll();

            List<ProductCategoryResponse> result = new ArrayList<>();
            productCategoryList.forEach(productCategory -> {
                ProductResponse productResponse = new ProductResponse();
                BeanUtils.copyProperties(productCategory.getProduct(), productResponse);

                CategoryResponse categoryResponse = new CategoryResponse();
                BeanUtils.copyProperties(productCategory.getCategory(), categoryResponse);

                ProductCategoryResponse response = ProductCategoryResponse.builder()
                        .id(productCategory.getId())
                        .product(productResponse)
                        .category(categoryResponse)
                        .build();

                result.add(response);
            });

            return ResponseUtil.build(PRODUCT_CATEGORY_GET_ALL, HttpStatus.OK, result);
        } catch (Exception e) {
            log.error("Error get all product category: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getOneById(Long id) {
        try {
            Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findById(id);

            if (productCategoryOptional.isEmpty())
                return ResponseUtil.build(PRODUCT_CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);

            ProductCategory productCategory = productCategoryOptional.get();
            ProductResponse productResponse = new ProductResponse();
            BeanUtils.copyProperties(productCategory.getProduct(), productResponse);

            CategoryResponse categoryResponse = new CategoryResponse();
            BeanUtils.copyProperties(productCategory.getCategory(), categoryResponse);

            ProductCategoryResponse response = ProductCategoryResponse.builder()
                    .id(productCategory.getId())
                    .product(productResponse)
                    .category(categoryResponse)
                    .build();

            return ResponseUtil.build(PRODUCT_CATEGORY_GET_BY_ID, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error get productCategory by id: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> updateOne(Long id, ProductCategoryRequest request) {
        try {
            log.info("Update productCategory {}", id);

            Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findById(id);
            if (productCategoryOptional.isEmpty())
                return ResponseUtil.build(PRODUCT_CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);

            Optional<Product> productOptional = productRepository.findById(request.getProductId());
            if (productOptional.isEmpty()) return ResponseUtil.build(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);

            Optional<Category> categoryOptional = categoryRepository.findById(request.getCategoryId());
            if (categoryOptional.isEmpty()) return ResponseUtil.build(CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);

            Product product = productOptional.get();
            Category category = categoryOptional.get();

            Optional<ProductCategory> productAndCategoryOptional = productCategoryRepository.findByProductAndCategoryAndIdNot(product, category, id);
            if (productAndCategoryOptional.isPresent())
                return ResponseUtil.build(PRODUCT_CATEGORY_EXIST, HttpStatus.BAD_REQUEST);

            ProductCategory productCategory = new ProductCategory();
            productCategory.setProduct(product);
            productCategory.setCategory(category);

            productCategory = productCategoryRepository.saveAndFlush(productCategory);

            ProductResponse productResponse = new ProductResponse();
            BeanUtils.copyProperties(productCategory.getProduct(), productResponse);

            CategoryResponse categoryResponse = new CategoryResponse();
            BeanUtils.copyProperties(productCategory.getCategory(), categoryResponse);

            ProductCategoryResponse response = ProductCategoryResponse.builder()
                    .id(productCategory.getId())
                    .product(productResponse)
                    .category(categoryResponse)
                    .build();

            return ResponseUtil.build(PRODUCT_CATEGORY_UPDATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error update product category with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> deleteOne(Long id) {
        try {
            log.info("Delete productCategory with id {}", id);

            Optional<ProductCategory> productCategoryOptional = productCategoryRepository.findById(id);
            if (productCategoryOptional.isEmpty())
                return ResponseUtil.build(PRODUCT_CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);

            productCategoryRepository.deleteById(id);
            return ResponseUtil.build(PRODUCT_CATEGORY_DELETED, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error delete productCategory with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }

    }
}
