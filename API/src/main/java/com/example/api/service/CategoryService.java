package com.example.api.service;

import com.example.api.entity.Category;
import com.example.api.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }
}
