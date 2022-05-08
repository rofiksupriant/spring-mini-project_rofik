package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.dao.Product;
import com.rofik.miniproject.domain.dto.request.ProductRequest;
import com.rofik.miniproject.domain.dto.response.ProductResponse;
import com.rofik.miniproject.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createOneSuccess() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).build();
        when(productRepository.saveAndFlush(any())).thenReturn(product);

        ProductRequest productRequest = new ProductRequest();
        ResponseEntity responseEntity = productService.createOne(productRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        ProductResponse data = (ProductResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
    }

    @Test
    void getAll() {
        List<Product> productList = Arrays.asList(
                Product.builder().id(1L).name("ANY1").deleted(false).build(),
                Product.builder().id(2L).name("ANY2").deleted(false).build(),
                Product.builder().id(3L).name("ANY3").deleted(false).build(),
                Product.builder().id(4L).name("ANY4").deleted(false).build(),
                Product.builder().id(5L).name("ANY5").deleted(false).build()
        );
        when(productRepository.findAll()).thenReturn(productList);

        ResponseEntity responseEntity = productService.getAll();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        List<ProductResponse> data = (List<ProductResponse>) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(5, data.size());
    }

    @Test
    void getAllDeleted() {
        List<Product> productList = Arrays.asList(
                Product.builder().id(1L).name("ANY1").deleted(true).build(),
                Product.builder().id(2L).name("ANY2").deleted(true).build(),
                Product.builder().id(3L).name("ANY3").deleted(true).build(),
                Product.builder().id(4L).name("ANY4").deleted(true).build(),
                Product.builder().id(5L).name("ANY5").deleted(true).build()
        );
        when(productRepository.findByDeleted(anyBoolean())).thenReturn(productList);

        ResponseEntity responseEntity = productService.getAllDeleted();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        List<ProductResponse> data = (List<ProductResponse>) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(5, data.size());
    }

    @Test
    void getOneByIdSuccess() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        ResponseEntity responseEntity = productService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        ProductResponse data = (ProductResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(1L, data.getId());
    }

    @Test
    void getOneByIdNotFound() {
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = productService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
    }

    @Test
    void updateOneSuccess() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.saveAndFlush(any())).thenReturn(product);

        ProductRequest request = new ProductRequest();
        ResponseEntity responseEntity = productService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        ProductResponse data = (ProductResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(1L, data.getId());
    }

    @Test
    void updateOneNotFound() {
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ProductRequest request = new ProductRequest();
        ResponseEntity responseEntity = productService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
    }

    @Test
    void deleteOneSuccess() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        ResponseEntity responseEntity = productService.deleteOne(6L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
    }

    @Test
    void deleteOneNotFound() {
        when(productRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = productService.deleteOne(6L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
    }
}