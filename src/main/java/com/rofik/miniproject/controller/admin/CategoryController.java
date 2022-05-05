package com.rofik.miniproject.controller.admin;

import com.rofik.miniproject.domain.dto.request.CategoryRequest;
import com.rofik.miniproject.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "v1/admin/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Object> createOne(@Valid @RequestBody CategoryRequest request) {
        return categoryService.createOne(request);
    }

    @GetMapping
    public ResponseEntity<Object> getAll() {
        return categoryService.getAll();
    }

    @GetMapping(path = "{id}")
    public ResponseEntity<Object> getOneById(@PathVariable(name = "id") Long id) {
        return categoryService.getOneById(id);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Object> updateOne(@PathVariable(name = "id") Long id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.updateOne(id, request);
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Object> deleteOne(@PathVariable(name = "id") Long id) {
        return categoryService.deleteOne(id);
    }
}
