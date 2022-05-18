package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.dao.Product;
import com.rofik.miniproject.domain.dto.request.ProductRequest;
import com.rofik.miniproject.domain.dto.response.ProductResponse;
import com.rofik.miniproject.repository.ProductRepository;
import com.rofik.miniproject.util.FileDownloadUtil;
import com.rofik.miniproject.util.FileUploadUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductServiceTest {
    @MockBean
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    private static MockedStatic<FileDownloadUtil> fileDownloadMock;
    private static MockedStatic<FileUploadUtil> fileUploadMock;
    private static MockMultipartFile image;

    @SneakyThrows
    @BeforeAll
    public static void init() {
        fileDownloadMock = Mockito.mockStatic(FileDownloadUtil.class);
        fileUploadMock = Mockito.mockStatic(FileUploadUtil.class);
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("test.png");
        image = new MockMultipartFile("test.png", "test.png", "image/png", inputStream);
    }

    @AfterAll
    public static void close() {
        fileDownloadMock.close();
        fileUploadMock.close();
    }

    @SneakyThrows
    @Test
    void createOneSuccess() {
        Product product = Product.builder().id(1L).name("ANY").image("path-to-file").deleted(false).build();

        fileUploadMock.when(() -> FileUploadUtil.saveFile(anyString(), anyString(), any())).thenReturn("path-to-file");
        fileDownloadMock.when(() -> FileDownloadUtil.getFileAsResource(anyString())).thenReturn(image.getResource());
        when(productRepository.saveAndFlush(any())).thenReturn(product);

        ProductRequest productRequest = ProductRequest.builder()
                .name("Product 1")
                .price(10000)
                .image(image)
                .build();

        ResponseEntity responseEntity = productService.createOne(productRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(PRODUCT_CREATED, apiResponse.getMessage());
    }

    @Test
    void createOneWithoutImageSuccess() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).build();
        when(productRepository.saveAndFlush(any())).thenReturn(product);

        ProductRequest productRequest = ProductRequest.builder()
                .name("Product 1")
                .price(10000)
                .build();

        ResponseEntity responseEntity = productService.createOne(productRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(PRODUCT_CREATED, apiResponse.getMessage());
    }

    @SneakyThrows
    @Test
    void createOneUploadImageError() {
        Product product = Product.builder().id(1L).name("ANY").image("path-to-file").deleted(false).build();

        fileUploadMock.when(() -> FileUploadUtil.saveFile(anyString(), anyString(), any())).thenThrow(IOException.class);

        when(productRepository.saveAndFlush(any())).thenReturn(product);
        try {
            ProductRequest productRequest = ProductRequest.builder()
                    .name("Product 1")
                    .price(10000)
                    .image(image)
                    .build();

            productService.createOne(productRequest);
        } catch (Exception e) {
        }
    }

    @SneakyThrows
    @Test
    void createOneDownloadImageError() {
        Product product = Product.builder().id(1L).name("ANY").image("path-to-file").deleted(false).build();

        fileUploadMock.when(() -> FileUploadUtil.saveFile(anyString(), anyString(), any())).thenReturn("path-to-file");
        fileDownloadMock.when(() -> FileDownloadUtil.getFileAsResource(any())).thenThrow(IOException.class);

        when(productRepository.saveAndFlush(any())).thenReturn(product);
        try {
            ProductRequest productRequest = ProductRequest.builder()
                    .name("Product 1")
                    .price(10000)
                    .image(image)
                    .build();

            productService.createOne(productRequest);
        } catch (Exception e) {
        }
    }

    @Test
    void getAllSuccess() {
        List<Product> productList = Arrays.asList(
                Product.builder().id(1L).name("ANY1").deleted(false).image("path-to-file").build(),
                Product.builder().id(2L).name("ANY2").deleted(false).image("path-to-file").build(),
                Product.builder().id(3L).name("ANY3").deleted(false).image("path-to-file").build(),
                Product.builder().id(4L).name("ANY4").deleted(false).image("path-to-file").build(),
                Product.builder().id(5L).name("ANY5").deleted(false).image("path-to-file").build()
        );
        when(productRepository.findAll()).thenReturn(productList);
        fileDownloadMock.when(() -> FileDownloadUtil.getFileAsResource(any())).thenReturn(image.getResource());

        ResponseEntity responseEntity = productService.getAll(false);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        List<ProductResponse> data = (List<ProductResponse>) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(PRODUCT_GET_ALL, apiResponse.getMessage());
        assertEquals(5, data.size());
    }

    @Test
    void getAllWithoutImageSuccess() {
        List<Product> productList = Arrays.asList(
                Product.builder().id(1L).name("ANY1").deleted(false).build(),
                Product.builder().id(2L).name("ANY2").deleted(false).build(),
                Product.builder().id(3L).name("ANY3").deleted(false).build(),
                Product.builder().id(4L).name("ANY4").deleted(false).build(),
                Product.builder().id(5L).name("ANY5").deleted(false).build()
        );
        when(productRepository.findAll()).thenReturn(productList);

        ResponseEntity responseEntity = productService.getAll(false);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        List<ProductResponse> data = (List<ProductResponse>) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(PRODUCT_GET_ALL, apiResponse.getMessage());
        assertEquals(5, data.size());
    }

    @Test
    void getAllError() {
        List<Product> productList = Arrays.asList(
                Product.builder().id(1L).name("ANY1").deleted(false).image("path-to-file").build(),
                Product.builder().id(2L).name("ANY2").deleted(false).image("path-to-file").build(),
                Product.builder().id(3L).name("ANY3").deleted(false).image("path-to-file").build(),
                Product.builder().id(4L).name("ANY4").deleted(false).image("path-to-file").build(),
                Product.builder().id(5L).name("ANY5").deleted(false).image("path-to-file").build()
        );
        when(productRepository.findAll()).thenReturn(productList);
        fileDownloadMock.when(() -> FileDownloadUtil.getFileAsResource(any())).thenThrow(IOException.class);

        try {
            ResponseEntity responseEntity = productService.getAll(false);
            ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
            List<ProductResponse> data = (List<ProductResponse>) apiResponse.getData();

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(HttpStatus.OK, apiResponse.getStatus());
            assertEquals(5, data.size());
        } catch (Exception e) {
        }
    }

    @Test
    void getOneByIdSuccess() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).image("path-to-file").build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        fileDownloadMock.when(() -> FileDownloadUtil.getFileAsResource(anyString())).thenReturn(image.getResource());

        ResponseEntity responseEntity = productService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(PRODUCT_GET_BY_ID, apiResponse.getMessage());
    }

    @Test
    void getOneByIdWithoutImageSuccess() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        ResponseEntity responseEntity = productService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(PRODUCT_GET_BY_ID, apiResponse.getMessage());
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
    void getOneByIdError() {
        Product product = Product.builder().id(1L).name("ANY").image("path-to-file").deleted(false).build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        fileDownloadMock.when(() -> FileDownloadUtil.getFileAsResource(anyString())).thenThrow(IOException.class);

        try {
            ResponseEntity responseEntity = productService.getOneById(1L);
            ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
            ProductResponse data = (ProductResponse) apiResponse.getData();

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(HttpStatus.OK, apiResponse.getStatus());
            assertEquals(1L, data.getId());
        } catch (Exception e) {
        }
    }

    @Test
    void updateOneSuccess() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).image("path-to-file").build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.saveAndFlush(any())).thenReturn(product);

        fileDownloadMock.when(() -> FileDownloadUtil.getFileAsResource(anyString())).thenReturn(image.getResource());
        fileUploadMock.when(() -> FileUploadUtil.saveFile(any(), any(), any())).thenReturn(product.getImage());

        ProductRequest productRequest = ProductRequest.builder()
                .name("Product 1")
                .price(10000)
                .image(image)
                .build();

        ResponseEntity responseEntity = productService.updateOne(1L, productRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        ProductResponse data = (ProductResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(1L, data.getId());
    }

    @Test
    void updateOneWithoutImageSuccess() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.saveAndFlush(any())).thenReturn(product);

        ProductRequest productRequest = ProductRequest.builder()
                .name("Product 1")
                .price(10000)
                .build();

        ResponseEntity responseEntity = productService.updateOne(1L, productRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(PRODUCT_UPDATED, apiResponse.getMessage());
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
    void updateOneDownloadFileError() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).image("path-to-file").build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.saveAndFlush(any())).thenReturn(product);

        fileUploadMock.when(() -> FileUploadUtil.saveFile(any(), any(), any())).thenReturn(product.getImage());
        fileDownloadMock.when(() -> FileDownloadUtil.getFileAsResource(anyString())).thenThrow(IOException.class);

        try {
            ProductRequest productRequest = ProductRequest.builder()
                    .name("Product 1")
                    .price(10000)
                    .image(image)
                    .build();

            productService.updateOne(1L, productRequest);

        } catch (Exception e) {
        }
    }

    @Test
    void updateOneUploadFileError() {
        Product product = Product.builder().id(1L).name("ANY").deleted(false).image("path-to-file").build();
        when(productRepository.findById(any())).thenReturn(Optional.of(product));
        when(productRepository.saveAndFlush(any())).thenReturn(product);

        fileUploadMock.when(() -> FileUploadUtil.saveFile(any(), any(), any())).thenThrow(IOException.class);
        fileDownloadMock.when(() -> FileDownloadUtil.getFileAsResource(anyString())).thenReturn(image.getResource());

        try {
            ProductRequest productRequest = ProductRequest.builder()
                    .name("Product 1")
                    .price(10000)
                    .image(image)
                    .build();

            productService.updateOne(1L, productRequest);

        } catch (Exception e) {
        }
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

    @Test
    void deleteOneError() {
        when(productRepository.findById(anyLong())).thenThrow(JpaObjectRetrievalFailureException.class);

        try {
            productService.deleteOne(1L);
        } catch (Exception e) {
        }
    }
}