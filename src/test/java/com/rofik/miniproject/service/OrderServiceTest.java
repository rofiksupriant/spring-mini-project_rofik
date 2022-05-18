package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.ApiResponse;
import com.rofik.miniproject.domain.common.OrderStatus;
import com.rofik.miniproject.domain.dao.Order;
import com.rofik.miniproject.domain.dao.Payment;
import com.rofik.miniproject.domain.dao.Product;
import com.rofik.miniproject.domain.dao.Table;
import com.rofik.miniproject.domain.dto.request.OrderRequest;
import com.rofik.miniproject.domain.dto.request.ProductOrderRequest;
import com.rofik.miniproject.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceTest {
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private PaymentRepository paymentRepository;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderDetailRepository orderDetailRepository;
    @MockBean
    private TableRepository tableRepository;
    @Autowired
    private OrderService orderService;

    @Test
    @WithMockUser(username = "d85d70c9-ce24-41a0-8cff-8efe4090f228", roles = {"CUSTOMER"})
    void createOneSuccess() {
        Payment payment = Payment.builder().id(1L).name("CASH").build();
        Product product = Product.builder().id(1L).name("Jus Jambu").price(10000).build();

        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(orderDetailRepository.saveAll(any())).thenReturn(new ArrayList<>());
        when(orderRepository.save(any())).thenReturn(Order.builder().id(1L).build());
        when(tableRepository.findByUuid(any())).thenReturn(Optional.of(
                Table.builder().uuid("d85d70c9-ce24-41a0-8cff-8efe4090f228").number(1).build()));

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

    }

    @Test
    @WithMockUser(username = "d85d70c9-ce24-41a0-8cff-8efe4090f228", roles = {"CUSTOMER"})
    void createOnePaymentNotFound() {
        Product product = Product.builder().id(1L).name("Jus Jambu").price(10000).build();

        when(paymentRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(orderDetailRepository.saveAll(any())).thenReturn(new ArrayList<>());
        when(orderRepository.save(any())).thenReturn(Order.builder().id(1L).build());
        when(tableRepository.findByUuid(any())).thenReturn(Optional.of(
                Table.builder().uuid("d85d70c9-ce24-41a0-8cff-8efe4090f228").number(1).build()));

        OrderRequest orderRequest = OrderRequest.builder()
                .paymentId(1L)
                .products(Arrays.asList(
                        ProductOrderRequest.builder().id(1L).quantity(1).build(),
                        ProductOrderRequest.builder().id(2L).quantity(1).build()
                ))
                .build();

        ResponseEntity responseEntity = orderService.createOne(orderRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(PAYMENT_NOT_FOUND, apiResponse.getMessage());

    }

    @Test
    @WithMockUser(username = "d85d70c9-ce24-41a0-8cff-8efe4090f228", roles = {"CUSTOMER"})
    void createOneProductNotFound() {
        Payment payment = Payment.builder().id(1L).name("CASH").build();
        Product product = Product.builder().id(1L).name("Jus Jambu").price(10000).build();

        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(payment));
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        when(orderDetailRepository.saveAll(any())).thenReturn(new ArrayList<>());
        when(orderRepository.save(any())).thenReturn(Order.builder().id(1L).build());
        when(tableRepository.findByUuid(any())).thenReturn(Optional.of(
                Table.builder().uuid("d85d70c9-ce24-41a0-8cff-8efe4090f228").number(1).build()));

        OrderRequest orderRequest = OrderRequest.builder()
                .paymentId(1L)
                .products(Arrays.asList(
                        ProductOrderRequest.builder().id(1L).quantity(1).build(),
                        ProductOrderRequest.builder().id(2L).quantity(1).build()
                ))
                .build();

        ResponseEntity responseEntity = orderService.createOne(orderRequest);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(PRODUCT_NOT_FOUND, apiResponse.getMessage());

    }

    @Test
    void createOneError() {
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
            fail();
        } catch (Exception e) {
        }
    }

    @Test
    void updateStatusOrderSuccess() {
        Table table = Table.builder().id(1L).number(1).build();
        Payment payment = Payment.builder().id(1L).name("CASH").build();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(
                Order.builder().id(1L).status(OrderStatus.ORDERED).table(table).payment(payment).build()
        ));
        when(orderRepository.saveAndFlush(any())).thenReturn(
                Order.builder().id(1L).status(OrderStatus.PAID).table(table).payment(payment).build()
        );
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(
                payment
        ));

        ResponseEntity responseEntity = orderService.updateStatus(1L, OrderStatus.PAID);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ORDER_UPDATED, apiResponse.getMessage());
    }

    @Test
    void updateStatusOrderNotFound() {
        Table table = Table.builder().id(1L).number(1).build();
        Payment payment = Payment.builder().id(1L).name("CASH").build();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));
        when(orderRepository.saveAndFlush(any())).thenReturn(
                Order.builder().id(1L).status(OrderStatus.PAID).table(table).payment(payment).build()
        );
        when(paymentRepository.findById(anyLong())).thenReturn(Optional.of(
                payment
        ));

        ResponseEntity responseEntity = orderService.updateStatus(1L, OrderStatus.PAID);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ORDER_NOT_FOUND, apiResponse.getMessage());
    }

    @Test
    void updateStatusOrderError() {
        doThrow(JpaObjectRetrievalFailureException.class).when(orderRepository).findById(anyLong());

        try {
            ResponseEntity responseEntity = orderService.updateStatus(1L, OrderStatus.PAID);
        } catch (Exception e) {
        }
    }

    @Test
    void getAllSuccess() {
        Table table = Table.builder().id(1L).number(1).build();
        Payment payment = Payment.builder().id(1L).name("CASH").build();

        when(orderRepository.findAll()).thenReturn(Arrays.asList(
                Order.builder().id(1L).table(table).payment(payment).status(OrderStatus.ORDERED).build()
        ));

        ResponseEntity<Object> responseEntity = orderService.getAll();
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ORDER_GET_ALL, apiResponse.getMessage());
    }

    @Test
    void getAllError() {
        doThrow(JpaObjectRetrievalFailureException.class).when(orderRepository).findAll();

        try {
            orderService.getAll();
        } catch (Exception e) {
        }
    }

    @Test
    void getOneSuccess() {
        Table table = Table.builder().id(1L).number(1).build();
        Payment payment = Payment.builder().id(1L).name("CASH").build();

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(
                Order.builder().id(1L).table(table).payment(payment).status(OrderStatus.ORDERED).build()));

        ResponseEntity<Object> responseEntity = orderService.getOne(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(ORDER_GET_BY_ID, apiResponse.getMessage());
    }

    @Test
    void getOneOrderNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.ofNullable(null));

        ResponseEntity<Object> responseEntity = orderService.getOne(1L);
        ApiResponse apiResponse = (ApiResponse) responseEntity.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals(ORDER_NOT_FOUND, apiResponse.getMessage());
    }

    @Test
    void getOneError() {
        doThrow(JpaObjectRetrievalFailureException.class).when(orderRepository).findById(anyLong());

        try {
            orderService.getOne(1L);
        } catch (Exception e) {
        }
    }
}