
//Huỳnh Tuấn Kiệt - 22110358

package com.example.api.service;

import com.example.api.entity.Category;
import com.example.api.entity.Product;
import com.example.api.repository.CategoryRepository;
import com.example.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService{
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
    public List<Product> findLastProducts() {
        return productRepository.findAllByOrderByCreateDateDesc();
    }
    public List<Product> findProductsByCategory(Long categoryId){
        Category category = categoryRepository.findByCategoryId(categoryId);
        if(category != null){
            return productRepository.findAllByCategory(category);
        }
        return new ArrayList<Product>();
    }


}
