package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.dao.Payment;
import com.rofik.miniproject.domain.dto.request.PaymentRequest;
import com.rofik.miniproject.domain.dto.response.PaymentResponse;
import com.rofik.miniproject.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PaymentServiceTest {
    @MockBean
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentService paymentService;

    @Test
    void createOneSuccess() {
        Payment category = Payment.builder().id(1L).name("CASH").status(true).build();
        when(paymentRepository.findByName(any())).thenReturn(Optional.ofNullable(null));
        when(paymentRepository.saveAndFlush(any())).thenReturn(category);

        PaymentRequest categoryRequest = new PaymentRequest();
        categoryRequest.setName("ANY");
        ResponseEntity responseEntity = paymentService.createOne(categoryRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        PaymentResponse data = (PaymentResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(PAYMENT_CREATED, apiResponse.getMessage());
        assertEquals("CASH", data.getName());
        assertTrue(data.getStatus());
    }

    @Test
    void createOneAlreadyExist() {
        Payment category = Payment.builder().id(1L).name("CASH").status(true).build();
        when(paymentRepository.findByName(any())).thenReturn(Optional.ofNullable(category));

        PaymentRequest categoryRequest = new PaymentRequest();
        categoryRequest.setName("ANY");

        ResponseEntity responseEntity = paymentService.createOne(categoryRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
        assertEquals(PAYMENT_EXIST, apiResponse.getMessage());
    }

    @Test
    void getAll() {
        List<Payment> categoryList = Arrays.asList(
                Payment.builder().id(1L).name("CASH").status(true).build(),
                Payment.builder().id(1L).name("OVO").status(false).build(),
                Payment.builder().id(1L).name("DANA").status(false).build()
        );
        when(paymentRepository.findAll()).thenReturn(categoryList);

        ResponseEntity responseEntity = paymentService.getAll();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        List<PaymentResponse> data = (List<PaymentResponse>) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(PAYMENT_GET_ALL, apiResponse.getMessage());
        assertEquals(3, data.size());
    }

    @Test
    void getOneByIdSuccess() {
        Payment category = Payment.builder().id(1L).name("CASH").status(true).build();
        when(paymentRepository.findById(any())).thenReturn(Optional.of(category));

        ResponseEntity responseEntity = paymentService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        PaymentResponse data = (PaymentResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(PAYMENT_GET_BY_ID, apiResponse.getMessage());
        assertEquals(1L, data.getId());
    }

    @Test
    void getOneByIdNotFound() {
        when(paymentRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = paymentService.getOneById(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(PAYMENT_NOT_FOUND, apiResponse.getMessage());
    }

    @Test
    void updateOneSuccess() {
        Payment category = Payment.builder().id(1L).name("CASH").status(true).build();
        when(paymentRepository.findById(any())).thenReturn(Optional.of(category));
        when(paymentRepository.findByNameAndIdNot(any(), any())).thenReturn(Optional.ofNullable(null));
        when(paymentRepository.saveAndFlush(any())).thenReturn(category);

        PaymentRequest request = new PaymentRequest();
        request.setName("ANY");
        request.setStatus(false);
        ResponseEntity responseEntity = paymentService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();
        PaymentResponse data = (PaymentResponse) apiResponse.getData();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(PAYMENT_UPDATED, apiResponse.getMessage());
        assertEquals("ANY", data.getName());
        assertFalse(data.getStatus());
    }

    @Test
    void updateOneNotFound() {
        when(paymentRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        PaymentRequest request = new PaymentRequest();
        request.setName("ANY");
        ResponseEntity responseEntity = paymentService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(PAYMENT_NOT_FOUND, apiResponse.getMessage());
    }

    @Test
    void updateOneNameAlreadyExist() {
        Payment category = Payment.builder().id(1L).name("CASH").status(true).build();
        when(paymentRepository.findById(any())).thenReturn(Optional.of(category));
        when(paymentRepository.findByNameAndIdNot(any(), any())).thenReturn(Optional.of(category));

        PaymentRequest request = new PaymentRequest();
        request.setName("ANY");
        ResponseEntity responseEntity = paymentService.updateOne(1L, request);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, apiResponse.getStatus());
        assertEquals(PAYMENT_EXIST, apiResponse.getMessage());
    }

    @Test
    void deleteOneSuccess() {
        Payment category = Payment.builder().id(1L).name("CASH").status(true).build();
        when(paymentRepository.findById(any())).thenReturn(Optional.of(category));

        ResponseEntity responseEntity = paymentService.deleteOne(6L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(HttpStatus.OK, apiResponse.getStatus());
        assertEquals(PAYMENT_DELETED, apiResponse.getMessage());
    }

    @Test
    void deleteOneNotFound() {
        when(paymentRepository.findById(any())).thenReturn(Optional.ofNullable(null));

        ResponseEntity responseEntity = paymentService.deleteOne(6L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, apiResponse.getStatus());
        assertEquals(PAYMENT_NOT_FOUND, apiResponse.getMessage());
    }
}