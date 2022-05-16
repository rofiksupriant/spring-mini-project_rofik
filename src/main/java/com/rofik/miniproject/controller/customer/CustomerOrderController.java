package com.rofik.miniproject.controller.customer;

import com.rofik.miniproject.domain.dto.request.OrderRequest;
import com.rofik.miniproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/customer/orders")
public class CustomerOrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Object> createOne(@RequestBody OrderRequest request) {
        return orderService.createOne(request);
    }
}
