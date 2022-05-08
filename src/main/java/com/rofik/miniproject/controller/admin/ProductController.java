package com.rofik.miniproject.controller.admin;

import com.rofik.miniproject.domain.dto.request.ProductRequest;
import com.rofik.miniproject.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "v1/admin/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<Object> createOne(@Valid @RequestBody ProductRequest request) {
        return productService.createOne(request);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return productService.getAll();
    }

    @GetMapping(value = "/deleted")
    public ResponseEntity<Object> getAllDeleted() {
        return productService.getAllDeleted();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(name = "id") Long id) {
        return productService.getOneById(id);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> updateOne(@PathVariable(name = "id") Long id, @Valid @RequestBody ProductRequest request) {
        return productService.updateOne(id, request);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> deleteOne(@PathVariable(name = "id") Long id) {
        return productService.deleteOne(id);
    }
}
