
//Huỳnh Tuấn Kiệt - 22110358
package com.example.api.controller;

import com.example.api.entity.Product;
import com.example.api.service.ProductService;
import com.example.api.utils.ErrorResponse;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//HuynhTuanKiet-22110358
@RestController
@RequestMapping(path = "/api/product")
public class ProductController {
    @Autowired
    ProductService productService;
    @GetMapping("/all-products")
    public ResponseEntity<?> getAllProducts() {
        List<Product> products = productService.findAllProducts();

        if (products == null || products.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "Product list is empty",
                    "No products found"
            );
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/last-products")
    public ResponseEntity<?> getLastProducts() {
        List<Product> products = productService.findLastProducts();

        if (products.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "No products found",
                    "No products available"
            );
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @GetMapping("/get-products-by-category/{categoryId}")
    public ResponseEntity<?> getProductsPaging(
            @PathVariable @Min(1) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "2") int size) {

        Page<Product> productPage = productService.findProductsByCategoryAndPrice(categoryId,page, size);

        if (productPage.isEmpty()) {
            ErrorResponse error = new ErrorResponse(
                    HttpStatus.NOT_FOUND.value(),
                    "No products found",
                    "No products available"
            );
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("products", productPage.getContent());
        response.put("currentPage", productPage.getNumber());
        response.put("totalItems", productPage.getTotalElements());
        response.put("totalPages", productPage.getTotalPages());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}