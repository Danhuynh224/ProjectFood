package com.example.app.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseBodyPaging {
    @SerializedName("products")
    private List<Product> products;
    @SerializedName("currentPage")
    private int currentPage;
    @SerializedName("totalItems")
    private int totalItems;
    @SerializedName("totalPages")
    private int totalPages;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(int totalItems) {
        this.totalItems = totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    // Getters and Setters
}