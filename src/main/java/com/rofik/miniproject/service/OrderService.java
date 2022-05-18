package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.OrderStatus;
import com.rofik.miniproject.domain.dao.*;
import com.rofik.miniproject.domain.dto.request.OrderRequest;
import com.rofik.miniproject.domain.dto.request.ProductOrderRequest;
import com.rofik.miniproject.domain.dto.response.OrderResponse;
import com.rofik.miniproject.domain.dto.response.PaymentResponse;
import com.rofik.miniproject.domain.dto.response.TableResponse;
import com.rofik.miniproject.repository.OrderDetailRepository;
import com.rofik.miniproject.repository.OrderRepository;
import com.rofik.miniproject.repository.PaymentRepository;
import com.rofik.miniproject.repository.ProductRepository;
import com.rofik.miniproject.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.*;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuthService authService;

    public ResponseEntity<Object> getAll() {
        try {
            Iterable<Order> orderIterable = orderRepository.findAll();

            List<OrderResponse> orderResponseList = new ArrayList<>();

            orderIterable.forEach(order -> {
                orderResponseList.add(
                        OrderResponse.builder()
                                .id(order.getId())
                                .totalQty(order.getTotalQty())
                                .totalPrice(order.getTotalPrice())
                                .payment(PaymentResponse.builder()
                                        .id(order.getPayment().getId())
                                        .name(order.getPayment().getName())
                                        .build())
                                .table(TableResponse.builder()
                                        .id(order.getTable().getId())
                                        .number(order.getTable().getNumber())
                                        .build())
                                .status(order.getStatus())
                                .build()
                );
            });

            return ResponseUtil.build(ORDER_GET_ALL, HttpStatus.OK, orderResponseList);
        } catch (Exception e) {
            log.error("Error get all orders : {}", e.getLocalizedMessage());
            throw e;
        }
    }

    @Transactional
    public ResponseEntity<Object> createOne(OrderRequest request) {
        try {
            Table me = authService.me();

            Optional<Payment> paymentOptional = paymentRepository.findById(request.getPaymentId());
            if (!paymentOptional.isPresent()) return ResponseUtil.build(PAYMENT_NOT_FOUND, HttpStatus.NOT_FOUND);

            Payment payment = paymentOptional.get();
            Order order = Order.builder()
                    .table(me)
                    .payment(payment)
                    .status(OrderStatus.ORDERED)
                    .build();

            Integer qty = 0;
            Integer totalPrice = 0;
            List<OrderDetail> orderDetailList = new ArrayList<>();
            for (ProductOrderRequest productOrderRequest : request.getProducts()) {
                Optional<Product> productOptional = productRepository.findById(productOrderRequest.getId());
                if (!productOptional.isPresent()) return ResponseUtil.build(PRODUCT_NOT_FOUND, HttpStatus.NOT_FOUND);

                Product product = productOptional.get();
                qty += productOrderRequest.getQuantity();
                totalPrice += (productOrderRequest.getQuantity() * product.getPrice());

                orderDetailList.add(OrderDetail.builder()
                        .order(order)
                        .product(product)
                        .image(product.getImage())
                        .price(product.getPrice())
                        .subtotal(product.getPrice() * productOrderRequest.getQuantity())
                        .quantity(productOrderRequest.getQuantity())
                        .build());
            }

            order.setTotalQty(qty);
            order.setTotalPrice(totalPrice);

            orderRepository.save(order);
            orderDetailRepository.saveAll(orderDetailList);

            OrderResponse orderResponse = OrderResponse.builder()
                    .id(order.getId())
                    .totalQty(order.getTotalQty())
                    .totalPrice(order.getTotalPrice())
                    .payment(PaymentResponse.builder()
                            .id(payment.getId())
                            .name(payment.getName())
                            .build())
                    .table(TableResponse.builder()
                            .id(order.getTable().getId())
                            .number(order.getTable().getNumber())
                            .build())
                    .status(order.getStatus())
                    .build();

            return ResponseUtil.build(ORDER_CREATED, HttpStatus.OK, orderResponse);
        } catch (Exception e) {
            log.error("Error create new order: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> updateStatus(Long orderId, OrderStatus orderStatus) {
        try {
            Optional<Order> orderOptional = orderRepository.findById(orderId);
            if (!orderOptional.isPresent()) return ResponseUtil.build(ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);

            Order order = orderOptional.get();
            order.setStatus(orderStatus);
            order = orderRepository.saveAndFlush(order);

            OrderResponse orderResponse = OrderResponse.builder()
                    .id(order.getId())
                    .totalQty(order.getTotalQty())
                    .totalPrice(order.getTotalPrice())
                    .payment(PaymentResponse.builder()
                            .id(order.getPayment().getId())
                            .name(order.getPayment().getName())
                            .build())
                    .table(TableResponse.builder()
                            .id(order.getTable().getId())
                            .number(order.getTable().getNumber())
                            .build())
                    .status(order.getStatus())
                    .build();

            return ResponseUtil.build(ORDER_UPDATED, HttpStatus.OK, orderResponse);
        } catch (Exception e) {
            log.error("error serve order: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getOne(Long id) {
        try {
            Optional<Order> orderOptional = orderRepository.findById(id);
            if (!orderOptional.isPresent()) return ResponseUtil.build(ORDER_NOT_FOUND, HttpStatus.NOT_FOUND);

            Order order = orderOptional.get();
            OrderResponse response = OrderResponse.builder()
                    .id(order.getId())
                    .totalQty(order.getTotalQty())
                    .totalPrice(order.getTotalPrice())
                    .payment(PaymentResponse.builder()
                            .id(order.getPayment().getId())
                            .name(order.getPayment().getName())
                            .build())
                    .table(TableResponse.builder()
                            .id(order.getTable().getId())
                            .number(order.getTable().getNumber())
                            .build())
                    .status(order.getStatus())
                    .build();

            return ResponseUtil.build(ORDER_GET_BY_ID, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error get all orders : {}", e.getLocalizedMessage());
            throw e;
        }
    }
}
