package com.rofik.miniproject.controller.admin;

import com.rofik.miniproject.domain.dto.request.PaymentRequest;
import com.rofik.miniproject.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "v1/admin/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Object> createOne(@Valid @RequestBody PaymentRequest request) {
        return paymentService.createOne(request);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return paymentService.getAll();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(name = "id") Long id) {
        return paymentService.getOneById(id);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> updateOne(@PathVariable(name = "id") Long id, @Valid @RequestBody PaymentRequest request) {
        return paymentService.updateOne(id, request);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> deleteOne(@PathVariable(name = "id") Long id) {
        return paymentService.deleteOne(id);
    }
}
