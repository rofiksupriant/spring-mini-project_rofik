package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.dao.Category;
import com.rofik.miniproject.domain.dao.Product;
import com.rofik.miniproject.domain.dao.ProductCategory;
import com.rofik.miniproject.domain.dto.request.ProductCategoryRequest;
import com.rofik.miniproject.domain.dto.response.ProductCategoryResponse;
import com.rofik.miniproject.repository.CategoryRepository;
import com.rofik.miniproject.repository.ProductCategoryRepository;
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

import static com.rofik.miniproject.constant.ResponseContant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductCategoryCategoryServiceTest {
    @Mock
    private ProductCategoryRepository productCategoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductCategoryService productCategoryService;

    @Test
    void createOneSuccess() {
        Product product = Product.builder().id(1L).name("ANY").build();
        Category category = Category.builder().id(2L).name("ANY").build();
        ProductCategory productCategory = ProductCategory.builder().product(product).category(category).build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productCategoryRepository.findByProductAndCategory(any(), any())).thenReturn(Optional.ofNullable(null));
        when(productCategoryRepository.saveAndFlush(any())).thenReturn(productCategory);

        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        productCategoryRequest.setProductId(1L);
        productCategoryRequest.setCategoryId(1L);

        ResponseEntity responseEntity = productCategoryService.createOne(productCategoryRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(PRODUCT_CATEGORY_CREATED, apiResponse.getMessage());
    }

    @Test
    void createOneAlreadyExist() {
        Product product = Product.builder().id(1L).name("ANY").build();
        Category category = Category.builder().id(2L).name("ANY").build();
        ProductCategory productCategory = ProductCategory.builder().product(product).category(category).build();

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productCategoryRepository.findByProductAndCategory(any(), any())).thenReturn(Optional.ofNullable(productCategory));
        when(productCategoryRepository.saveAndFlush(any())).thenReturn(productCategory);

        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        productCategoryRequest.setProductId(1L);
        productCategoryRequest.setCategoryId(1L);

        ResponseEntity responseEntity = productCategoryService.createOne(productCategoryRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(PRODUCT_CATEGORY_EXIST, apiResponse.getMessage());
    }

    @Test
    void getAll() {
        List<ProductCategory> productCategoryList = Arrays.asList(
                ProductCategory.builder().id(1L).product(new Product()).category(new Category()).build(),
                ProductCategory.builder().id(2L).product(new Product()).category(new Category()).build(),
                ProductCategory.builder().id(3L).product(new Product()).category(new Category()).build(),
                ProductCategory.builder().id(4L).product(new Product()).category(new Category()).build(),
                ProductCategory.builder().id(5L).product(new Product()).category(new Category()).build()
        );
        when(productCategoryRepository.findAll()).thenReturn(productCategoryList);

        ResponseEntity responseEntity = productCategoryService.getAll();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        List<ProductCategoryResponse> data = (List<ProductCategoryResponse>) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(PRODUCT_CATEGORY_GET_ALL, apiResponse.getMessage());
        assertEquals(5, data.size());
    }

    @Test
    void getOneByIdSuccess() {
        ProductCategory productCategory = ProductCategory.builder().id(1L).product(new Product()).category(new Category()).build();
        when(productCategoryRepository.findById(any())).thenReturn(Optional.of(productCategory));

        ResponseEntity responseEntity = productCategoryService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(PRODUCT_CATEGORY_GET_BY_ID, apiResponse.getMessage());
    }

    @Test
    void getOneByIdNotFound() {
        when(productCategoryRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = productCategoryService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(PRODUCT_CATEGORY_NOT_FOUND, apiResponse.getMessage());
    }

    @Test
    void updateOneSuccess() {
        Product product = Product.builder().name("ANY").build();
        Category category = Category.builder().name("ANY").build();
        ProductCategory productCategory = ProductCategory.builder().product(product).category(category).build();

        when(productCategoryRepository.findById(anyLong())).thenReturn(Optional.of(productCategory));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productCategoryRepository.findByProductAndCategory(any(), any())).thenReturn(Optional.ofNullable(null));
        when(productCategoryRepository.saveAndFlush(any())).thenReturn(productCategory);

        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        productCategoryRequest.setProductId(1L);
        productCategoryRequest.setCategoryId(1L);

        ResponseEntity responseEntity = productCategoryService.updateOne(1L, productCategoryRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(PRODUCT_CATEGORY_UPDATED, apiResponse.getMessage());
    }

    @Test
    void updateOneAlreadyExist() {
        Product product = Product.builder().name("ANY").build();
        Category category = Category.builder().name("ANY").build();
        ProductCategory productCategory = ProductCategory.builder().product(product).category(category).build();

        when(productCategoryRepository.findById(anyLong())).thenReturn(Optional.of(productCategory));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productCategoryRepository.findByProductAndCategoryAndIdNot(any(), any(), anyLong())).thenReturn(Optional.ofNullable(productCategory));
        when(productCategoryRepository.saveAndFlush(any())).thenReturn(productCategory);

        ProductCategoryRequest productCategoryRequest = new ProductCategoryRequest();
        productCategoryRequest.setProductId(1L);
        productCategoryRequest.setCategoryId(1L);

        ResponseEntity responseEntity = productCategoryService.updateOne(1L, productCategoryRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(PRODUCT_CATEGORY_EXIST, apiResponse.getMessage());
    }

    @Test
    void updateOneNotFound() {
        when(productCategoryRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ProductCategoryRequest request = new ProductCategoryRequest();
        ResponseEntity responseEntity = productCategoryService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(PRODUCT_CATEGORY_NOT_FOUND, apiResponse.getMessage());
    }

    @Test
    void deleteOneSuccess() {
        ProductCategory productCategory = ProductCategory.builder().product(new Product()).category(new Category()).build();
        when(productCategoryRepository.findById(any())).thenReturn(Optional.of(productCategory));

        ResponseEntity responseEntity = productCategoryService.deleteOne(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(PRODUCT_CATEGORY_DELETED, apiResponse.getMessage());
    }

    @Test
    void deleteOneNotFound() {
        when(productCategoryRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = productCategoryService.deleteOne(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(PRODUCT_CATEGORY_NOT_FOUND, apiResponse.getMessage());
    }
}