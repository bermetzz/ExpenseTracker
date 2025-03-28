package com.example.expensetracker.service;

import com.example.expensetracker.dto.CategoryRequest;
import com.example.expensetracker.entity.Category;
import com.example.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category createCategory(CategoryRequest request) {
        Category category = Category.builder()
                .name(request.name())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
