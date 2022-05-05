package com.rofik.miniproject.service;

import com.rofik.miniproject.domain.dao.Category;
import com.rofik.miniproject.domain.dto.request.CategoryRequest;
import com.rofik.miniproject.domain.dto.response.CategoryResponse;
import com.rofik.miniproject.repository.CategoryRepository;
import com.rofik.miniproject.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rofik.miniproject.constant.ResponseContant.*;

@Slf4j
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public ResponseEntity<Object> createOne(CategoryRequest request) {
        try {
            Optional<Category> categoryOptional = categoryRepository.findByName(request.getName());
            if (categoryOptional.isPresent())
                return ResponseUtil.build(CATEGORY_EXIST, HttpStatus.BAD_REQUEST);

            Category category = new Category();
            BeanUtils.copyProperties(request, category);
            category = categoryRepository.saveAndFlush(category);

            CategoryResponse response = new CategoryResponse();
            BeanUtils.copyProperties(category, response);
            return ResponseUtil.build(CATEGORY_CREATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error create new category: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getAll() {
        try {
            log.info("Get all category");
            List<Category> categoryList = categoryRepository.findAll();

            List<CategoryResponse> result = new ArrayList<>();
            categoryList.forEach(category -> result.add(
                    CategoryResponse.builder()
                            .id(category.getId())
                            .name(category.getName())
                            .description(category.getDescription())
                            .build()
            ));

            return ResponseUtil.build(CATEGORY_GET_ALL, HttpStatus.OK, result);
        } catch (Exception e) {
            log.error("Error get all category: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> getOneById(Long id) {
        try {
            Optional<Category> categoryOptional = categoryRepository.findById(id);

            if (categoryOptional.isEmpty()) return ResponseUtil.build(CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);

            Category category = categoryOptional.get();
            CategoryResponse response = new CategoryResponse();
            BeanUtils.copyProperties(category, response);

            return ResponseUtil.build(CATEGORY_GET_BY_ID, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error get category by id: {}", e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> updateOne(Long id, CategoryRequest request) {
        try {
            log.info("Update category {}", id);

            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if (categoryOptional.isEmpty()) return ResponseUtil.build(CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);

            Optional<Category> categoryOptionalByName = categoryRepository.findByNameAndIdNot(request.getName(), id);
            if (categoryOptionalByName.isPresent())
                return ResponseUtil.build(CATEGORY_EXIST, HttpStatus.BAD_REQUEST);

            Category category = categoryOptional.get();
            BeanUtils.copyProperties(request, category);
            category = categoryRepository.saveAndFlush(category);

            CategoryResponse response = new CategoryResponse();
            BeanUtils.copyProperties(category, response);
            return ResponseUtil.build(CATEGORY_UPDATED, HttpStatus.OK, response);
        } catch (Exception e) {
            log.error("Error update category with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }
    }

    public ResponseEntity<Object> deleteOne(Long id) {
        try {
            log.info("Delete category with id {}", id);

            Optional<Category> categoryOptional = categoryRepository.findById(id);
            if (categoryOptional.isEmpty()) return ResponseUtil.build(CATEGORY_NOT_FOUND, HttpStatus.NOT_FOUND);

            categoryRepository.deleteById(id);
            return ResponseUtil.build(CATEGORY_DELETED, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error delete category with id {}: {}", id, e.getLocalizedMessage());
            throw e;
        }

    }
}
