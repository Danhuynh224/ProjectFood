
//Huỳnh Tuấn Kiệt - 22110358

package com.example.api.service;

import com.example.api.entity.Category;
import com.example.api.entity.Product;
import com.example.api.repository.CategoryRepository;
import com.example.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
    public Page<Product> findProductsByCategoryAndPrice(Long categoryId ,int page, int size) {
        Category category = categoryRepository.findByCategoryId(categoryId);
        if(category != null){
            System.out.println("hello");
            Pageable pageable = PageRequest.of(page, size, Sort.by("price").ascending());
            return productRepository.findAllByCategory(category,pageable);
        }
        return new PageImpl<>(new ArrayList<Product>());
    }

}
