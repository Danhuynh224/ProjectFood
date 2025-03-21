package com.example.api.controller;

import com.example.api.entity.Product;
import com.example.api.service.ProductService;
import com.example.api.utils.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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

}