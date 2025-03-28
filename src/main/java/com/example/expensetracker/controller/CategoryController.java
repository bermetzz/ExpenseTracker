package com.example.expensetracker.controller;

import com.example.expensetracker.dto.CategoryRequest;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public Category createCategory(@RequestBody CategoryRequest request) {
        return categoryService.createCategory(request);
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
