package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.common.OrderStatus;
import com.rofik.miniproject.domain.dao.*;
import com.rofik.miniproject.domain.dto.request.OrderRequest;
import com.rofik.miniproject.domain.dto.request.ProductOrderRequest;
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
                        .image(product.getPicture())
                        .price(product.getPrice())
                        .quantity(productOrderRequest.getQuantity())
                        .build());
            }

            order.setTotalQty(qty);
            order.setTotalPrice(totalPrice);

            orderRepository.save(order);
            orderDetailRepository.saveAll(orderDetailList);

            return ResponseUtil.build(ORDER_CREATED, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error create new order: {}", e.getLocalizedMessage());
            throw e;
        }
    }

//    public ResponseEntity<Object> getAll() {
//        try {
//            log.info("Get all order");
//            List<Category> orderList = orderRepository.findAll();
//
//            List<CategoryResponse> result = new ArrayList<>();
//            orderList.forEach(order -> result.add(
//                    CategoryResponse.builder()
//                            .id(order.getId())
//                            .name(order.getName())
//                            .description(order.getDescription())
//                            .build()
//            ));
//
//            return ResponseUtil.build(CATEGORY_GET_ALL, HttpStatus.OK, result);
//        } catch (Exception e) {
//            log.error("Error get all order: {}", e.getLocalizedMessage());
//            throw e;
//        }
//    }
//
//    public ResponseEntity<Object> getOneById(Long id) {
//        try {
//            Optional<Category> orderOptional = orderRepository.findById(id);
//
//            if (orderOptional.isEmpty()) return ResponseUtil.build(CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
//
//            Category order = orderOptional.get();
//            CategoryResponse response = new CategoryResponse();
//            BeanUtils.copyProperties(order, response);
//
//            return ResponseUtil.build(CATEGORY_GET_BY_ID, HttpStatus.OK, response);
//        } catch (Exception e) {
//            log.error("Error get order by id: {}", e.getLocalizedMessage());
//            throw e;
//        }
//    }
//
//    public ResponseEntity<Object> updateOne(Long id, CategoryRequest request) {
//        try {
//            log.info("Update order {}", id);
//
//            Optional<Category> orderOptional = orderRepository.findById(id);
//            if (orderOptional.isEmpty()) return ResponseUtil.build(CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
//
//            Optional<Category> orderOptionalByName = orderRepository.findByNameAndIdNot(request.getName(), id);
//            if (orderOptionalByName.isPresent())
//                return ResponseUtil.build(CATEGORY_EXIST, HttpStatus.BAD_REQUEST);
//
//            Category order = orderOptional.get();
//            BeanUtils.copyProperties(request, order);
//            order = orderRepository.saveAndFlush(order);
//
//            CategoryResponse response = new CategoryResponse();
//            BeanUtils.copyProperties(order, response);
//            return ResponseUtil.build(CATEGORY_UPDATED, HttpStatus.OK, response);
//        } catch (Exception e) {
//            log.error("Error update order with id {}: {}", id, e.getLocalizedMessage());
//            throw e;
//        }
//    }
//
//    public ResponseEntity<Object> deleteOne(Long id) {
//        try {
//            log.info("Delete order with id {}", id);
//
//            Optional<Category> orderOptional = orderRepository.findById(id);
//            if (orderOptional.isEmpty()) return ResponseUtil.build(CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);
//
//            orderRepository.deleteById(id);
//            return ResponseUtil.build(CATEGORY_DELETED, HttpStatus.OK);
//        } catch (Exception e) {
//            log.error("Error delete order with id {}: {}", id, e.getLocalizedMessage());
//            throw e;
//        }
//
//    }
}
