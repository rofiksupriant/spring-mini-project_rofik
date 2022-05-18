package com.rofik.miniproject.controller.employee;

import com.rofik.miniproject.domain.common.OrderStatus;
import com.rofik.miniproject.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/employee/orders")
public class EmployeeOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return orderService.getAll();
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getOne(@PathVariable(name = "id") Long id) {
        return orderService.getOne(id);
    }

    @PutMapping("{id}/pay")
    public ResponseEntity<Object> payOrder(@PathVariable(name = "id") Long id) {
        return orderService.updateStatus(id, OrderStatus.PAID);
    }

    @PutMapping("{id}/process")
    public ResponseEntity<Object> processOrder(@PathVariable(name = "id") Long id) {
        return orderService.updateStatus(id, OrderStatus.PROCESS);
    }
}
