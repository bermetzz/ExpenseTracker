package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategoryRequest;
import com.example.expensetracker.entity.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryRequest request);
    List<Category> getAllCategories();
}
