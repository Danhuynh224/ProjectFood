//Huỳnh Tuấn Kiệt - 22110358

package com.example.api.repository;

import com.example.api.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();
    Category findByCategoryId(Long categoryId);
}
