package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.dao.Payment;
import com.rofik.miniproject.domain.dao.Product;
import com.rofik.miniproject.domain.dto.request.OrderRequest;
import com.rofik.miniproject.domain.dto.request.ProductOrderRequest;
import com.rofik.miniproject.repository.PaymentRepository;
import com.rofik.miniproject.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.ORDER_CREATED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceTest {
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderService orderService;

    @Test
    @WithMockUser(username = "d85d70c9-ce24-41a0-8cff-8efe4090f228", roles = {"CUSTOMER"})
    void createOneSuccess() {
        Payment payment = Payment.builder().id(1L).name("CASH").build();
        Product product = Product.builder().id(1L).name("Jus Jambu").price(10000).build();

        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        try {
            OrderRequest orderRequest = OrderRequest.builder()
                    .paymentId(1L)
                    .products(Arrays.asList(
                            ProductOrderRequest.builder().id(1L).quantity(1).build(),
                            ProductOrderRequest.builder().id(2L).quantity(1).build()
                    ))
                    .build();

            ResponseEntity responseEntity = orderService.createOne(orderRequest);
            ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(ORDER_CREATED, apiResponse.getMessage());
        } catch (Exception e) {
            fail();
        }
    }
}