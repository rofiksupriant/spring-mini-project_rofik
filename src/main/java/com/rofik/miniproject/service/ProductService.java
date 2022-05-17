package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.dao.Product;
import com.rofik.miniproject.domain.dto.request.ProductRequest;
import com.rofik.miniproject.domain.dto.response.ProductResponse;
import com.rofik.miniproject.repository.ProductRepository;
import com.rofik.miniproject.util.FileDownloadUtil;
import com.rofik.miniproject.util.FileUploadUtil;
import com.rofik.miniproject.util.ResponseUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.*;

@Slf4j
@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EntityManager entityManager;

    @SneakyThrows
    @Transactional
    public ResponseEntity<Object> createOne(ProductRequest request) {
        try {
            Product product = new Product();
            BeanUtils.copyProperties(request, product);
            if (request.getImage() != null) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(request.getImage().getOriginalFilename()));
                String filePath = FileUploadUtil.saveFile("products", fileName, request.getImage());
                product.setImage(filePath);
            }
            product = productRepository.saveAndFlush(product);

            Resource fileAsResource = null;
            if (product.getImage() != null) {
                try {
                    fileAsResource = FileDownloadUtil.getFileAsResource(product.getImage());
                } catch (IOException e) {
                    log.error("error load image file: {}", e.getLocalizedMessage());
                }
            }

            ProductResponse response = new ProductResponse();
            BeanUtils.copyProperties(product, response);
            response.setImage(fileAsResource);

            return ResponseUtil.build(PRODUCT_CREATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error create new product: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getAll(boolean isDeleted) {
        try {
            log.info("Get all product where deleted {}", isDeleted);
            Session session = entityManager.unwrap(Session.class);
            Filter filter = session.enableFilter("deletedProductFilter");
            filter.setParameter("isDeleted", isDeleted);
            Iterable<Product> products = productRepository.findAll();
            session.disableFilter("deletedProductFilter");

            List<ProductResponse> result = new ArrayList<>();
            products.forEach(product -> {
                Resource fileAsResource = null;
                if (product.getImage() != null) {
                    try {
                        fileAsResource = FileDownloadUtil.getFileAsResource(product.getImage());
                    } catch (IOException e) {
                        log.error("error load image file: {}", e.getLocalizedMessage());
                    }
                }
                result.add(
                        ProductResponse.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .description(product.getDescription())
                                .volume(product.getVolume())
                                .weight(product.getWeight())
                                .image(fileAsResource)
                                .build()
                );
            });

            return ResponseUtil.build(PRODUCT_GET_ALL, HttpStatus.OK, result);
        } catch (Exception e) {
            log.error("Error get all product where deleted {}: {}", isDeleted, e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getOneById(Long id) {
        try {
            Optional<Product> productOptional = productRepository.findById(id);

            if (productOptional.isEmpty()) return ResponseUtil.build(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);

            Product product = productOptional.get();
            Resource fileAsResource = null;
            if (product.getImage() != null) {
                try {
                    fileAsResource = FileDownloadUtil.getFileAsResource(product.getImage());
                } catch (IOException e) {
                    log.error("error load image file: {}", e.getLocalizedMessage());
                }
            }
            ProductResponse response = ProductResponse.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .description(product.getDescription())
                    .volume(product.getVolume())
                    .weight(product.getWeight())
                    .image(fileAsResource)
                    .build();

            return ResponseUtil.build(PRODUCT_GET_BY_ID, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error get product by id: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    @SneakyThrows
    public ResponseEntity<Object> updateOne(Long id, ProductRequest request) {
        try {
            log.info("Update product {}", id);

            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isEmpty()) return ResponseUtil.build(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);

            Product product = productOptional.get();
            BeanUtils.copyProperties(request, product);
            if (request.getImage() != null) {
                String fileName = StringUtils.cleanPath(Objects.requireNonNull(request.getImage().getOriginalFilename()));
                String filePath = FileUploadUtil.saveFile("products", fileName, request.getImage());
                product.setImage(filePath);
            }
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
