<<<<<<< HEAD
//Huỳnh Tuấn Kiệt - 22110358
=======
>>>>>>> a9919c7f42d9d5ba1067beae7910aa5256e2e76f
package com.example.api.service;

import com.example.api.entity.Product;
import com.example.api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
