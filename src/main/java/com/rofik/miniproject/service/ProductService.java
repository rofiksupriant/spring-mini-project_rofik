package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.dao.Product;
import com.rofik.miniproject.domain.dto.request.ProductRequest;
import com.rofik.miniproject.domain.dto.response.ProductResponse;
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
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public ResponseEntity<Object> createOne(ProductRequest request) {
        try {
            Product product = new Product();
            BeanUtils.copyProperties(request, product);
            product = productRepository.saveAndFlush(product);

            ProductResponse response = new ProductResponse();
            BeanUtils.copyProperties(product, response);
            return ResponseUtil.build(PRODUCT_CREATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error create new product: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getAll() {
        try {
            log.info("Get all product");
            List<Product> productList = productRepository.findAll();

            List<ProductResponse> result = new ArrayList<>();
            productList.forEach(product -> result.add(
                    ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .description(product.getDescription())
                            .volume(product.getVolume())
                            .weight(product.getWeight())
                            .picture(product.getPicture())
                            .build()
            ));

            return ResponseUtil.build(PRODUCT_GET_ALL, HttpStatus.OK, result);
        } catch (Exception e) {
            log.error("Error get all product: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getAllDeleted() {
        try {
            log.info("Get all product");
            List<Product> productList = productRepository.findByDeleted(true);

            List<ProductResponse> result = new ArrayList<>();
            productList.forEach(product -> result.add(
                    ProductResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .description(product.getDescription())
                            .volume(product.getVolume())
                            .weight(product.getWeight())
                            .picture(product.getPicture())
                            .build()
            ));

            return ResponseUtil.build(PRODUCT_GET_ALL_DELETED, HttpStatus.OK, result);
        } catch (Exception e) {
            log.error("Error get all product: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getOneById(Long id) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);

            if (productOptional.isEmpty()) return ResponseUtil.build(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);

            Product product = productOptional.get();
            ProductResponse response = new ProductResponse();
            BeanUtils.copyProperties(product, response);

            return ResponseUtil.build(PRODUCT_GET_BY_ID, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error get product by id: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> updateOne(Long id, ProductRequest request) {
        try {
            log.info("Update product {}", id);

            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) return ResponseUtil.build(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);


            Product product = productOptional.get();
            BeanUtils.copyProperties(request, product);
            product = productRepository.saveAndFlush(product);

            ProductResponse response = new ProductResponse();
            BeanUtils.copyProperties(product, response);
            return ResponseUtil.build(PRODUCT_UPDATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error update product with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> deleteOne(Long id) {
        try {
            log.info("Delete product with id {}", id);

            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) return ResponseUtil.build(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);

            productRepository.deleteById(id);
            return ResponseUtil.build(PRODUCT_DELETED, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error delete product with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }

    }
}
