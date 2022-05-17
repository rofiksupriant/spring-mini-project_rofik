package com.rofik.miniproject.controller.customer;

import com.rofik.miniproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/v1/customer/products")
public class CustomerProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return productService.getAll();
    }

    @GetMapping(value = "{id}")
    public ResponseEntity<Object> getOne(@PathVariable(name = "id") Long id) {
        return productService.getOneById(id);
    }
}
