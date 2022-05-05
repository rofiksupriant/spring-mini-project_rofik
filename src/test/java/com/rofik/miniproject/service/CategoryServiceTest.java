package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.dao.Category;
import com.rofik.miniproject.domain.dto.request.CategoryRequest;
import com.rofik.miniproject.domain.dto.response.CategoryResponse;
import com.rofik.miniproject.repository.CategoryRepository;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void createOneSuccess() {
        Category category = Category.builder().id(1L).name("FOOD").description("FOOD Description").build();
        when(categoryRepository.findByName(any())).thenReturn(Optional.ofNullable(null));
        when(categoryRepository.saveAndFlush(any())).thenReturn(category);

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("ANY");
        ResponseEntity responseEntity = categoryService.createOne(categoryRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        CategoryResponse data = (CategoryResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(CATEGORY_CREATED, apiResponse.getMessage());
        assertEquals("FOOD", data.getName());
    }

    @Test
    void createOneAlreadyExist() {
        Category category = Category.builder().id(1L).name("FOOD").description("FOOD Description").build();
        when(categoryRepository.findByName(any())).thenReturn(Optional.ofNullable(category));

        CategoryRequest categoryRequest = new CategoryRequest();
        categoryRequest.setName("ANY");

        ResponseEntity responseEntity = categoryService.createOne(categoryRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
        assertEquals(CATEGORY_EXIST, apiResponse.getMessage());
    }

    @Test
    void getAll() {
        List<Category> categoryList = Arrays.asList(
                Category.builder().id(1L).name("FOOD").description("FOOD Description").build(),
                Category.builder().id(2L).name("DRINK").description("FOOD Description").build(),
                Category.builder().id(3L).name("COFFEE").description("FOOD Description").build()
        );
        when(categoryRepository.findAll()).thenReturn(categoryList);

        ResponseEntity responseEntity = categoryService.getAll();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        List<CategoryResponse> data = (List<CategoryResponse>) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(CATEGORY_GET_ALL, apiResponse.getMessage());
        assertEquals(3, data.size());
    }

    @Test
    void getOneByIdSuccess() {
        Category category = Category.builder().id(1L).name("FOOD").description("FOOD Description").build();
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        ResponseEntity responseEntity = categoryService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        CategoryResponse data = (CategoryResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(CATEGORY_GET_BY_ID, apiResponse.getMessage());
        assertEquals(1L, data.getId());
    }

    @Test
    void getOneByIdNotFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = categoryService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(CATEGORY_NOT_FOUND, apiResponse.getMessage());
    }

    @Test
    void updateOneSuccess() {
        Category category = Category.builder().id(1L).name("FOOD").description("FOOD Description").build();
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(categoryRepository.findByNameAndIdNot(any(), any())).thenReturn(Optional.ofNullable(null));
        when(categoryRepository.saveAndFlush(any())).thenReturn(category);

        CategoryRequest request = new CategoryRequest();
        request.setName("DRINK");
        ResponseEntity responseEntity = categoryService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        CategoryResponse data = (CategoryResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(CATEGORY_UPDATED, apiResponse.getMessage());
        assertEquals("DRINK", data.getName());
    }

    @Test
    void updateOneNotFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        CategoryRequest request = new CategoryRequest();
        request.setName("ANY");
        ResponseEntity responseEntity = categoryService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(CATEGORY_NOT_FOUND, apiResponse.getMessage());
    }

    @Test
    void updateOneNameAlreadyExist() {
        Category category = Category.builder().id(1L).name("FOOD").description("FOOD Description").build();
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));
        when(categoryRepository.findByNameAndIdNot(any(), any())).thenReturn(Optional.of(category));

        CategoryRequest request = new CategoryRequest();
        request.setName("ANY");
        ResponseEntity responseEntity = categoryService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
        assertEquals(CATEGORY_EXIST, apiResponse.getMessage());
    }

    @Test
    void deleteOneSuccess() {
        Category category = Category.builder().id(1L).name("FOOD").description("FOOD Description").build();
        when(categoryRepository.findById(any())).thenReturn(Optional.of(category));

        ResponseEntity responseEntity = categoryService.deleteOne(6L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(CATEGORY_DELETED, apiResponse.getMessage());
    }

    @Test
    void deleteOneNotFound() {
        when(categoryRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = categoryService.deleteOne(6L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(CATEGORY_NOT_FOUND, apiResponse.getMessage());
    }
}