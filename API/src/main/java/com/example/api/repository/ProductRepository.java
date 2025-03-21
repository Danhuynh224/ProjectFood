package com.example.api.repository;


import com.example.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//HuynhTuanKiet-22110358
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
    List<Product> findAllByOrderByCreateDateDesc();
}