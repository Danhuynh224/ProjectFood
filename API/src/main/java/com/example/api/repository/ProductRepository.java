
//Huỳnh Tuấn Kiệt - 22110358

package com.example.api.repository;


import com.example.api.entity.Category;
import com.example.api.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//HuynhTuanKiet-22110358
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByCategory(Category category,Pageable pageable);
    List<Product> findAllByOrderByCreateDateDesc();
    List<Product> findAllByCategory(Category category);
}