package com.rofik.miniproject.controller.admin;

import com.rofik.miniproject.domain.dto.request.ProductCategoryRequest;
import com.rofik.miniproject.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "v1/admin/product-categories")
public class ProductCategoryController {
    @Autowired
    private ProductCategoryService productCategoryService;

    @PostMapping
    public ResponseEntity<Object> createOne(@Valid @RequestBody ProductCategoryRequest request) {
        return productCategoryService.createOne(request);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return productCategoryService.getAll();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(name = "id") Long id) {
        return productCategoryService.getOneById(id);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> updateOne(@PathVariable(name = "id") Long id, @Valid @RequestBody ProductCategoryRequest request) {
        return productCategoryService.updateOne(id, request);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> deleteOne(@PathVariable(name = "id") Long id) {
        return productCategoryService.deleteOne(id);
    }
}
