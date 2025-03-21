<<<<<<< HEAD
<<<<<<< HEAD
//Huỳnh Tuấn Kiệt - 22110358
=======
>>>>>>> a9919c7f42d9d5ba1067beae7910aa5256e2e76f
=======
//Huỳnh Tuấn Kiệt - 22110358
>>>>>>> dev_dan
package com.example.api.repository;


import com.example.api.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAll();
    List<Product> findAllByOrderByCreateDateDesc();
}