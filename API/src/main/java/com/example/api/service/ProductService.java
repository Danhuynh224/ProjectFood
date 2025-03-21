
//Huỳnh Tuấn Kiệt - 22110358

package com.example.api.service;

import com.example.api.entity.Product;
import com.example.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService{
    @Autowired
    ProductRepository productRepository;
    public List<Product> findAllProducts() {
        return productRepository.findAll();
    }
    public List<Product> findLastProducts() {
        return productRepository.findAllByOrderByCreateDateDesc();
    }
    public Page<Product> findProductsByPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        return productRepository.findAll(pageable);
    }

}
