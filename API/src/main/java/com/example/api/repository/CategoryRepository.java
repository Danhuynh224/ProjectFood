package com.example.api.repository;

import com.example.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
//HuynhTuanKiet-22110358
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();
}
