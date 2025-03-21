//Nguyễn Phan Minh Trí - 22110443
package com.example.app.API;

import com.example.app.Model.Category;
import com.example.app.Model.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceAPI {
    @GET("/api/category/all-categories")
    Call<List<Category>> getCategoriesAll();
    @GET("/api/product/last-products")
    Call<List<Product>> getLastProducts();
    @GET("/api/product/get-products-by-category/{categoryId}") // Sửa lỗi API
    Call<List<Product>> getProductByCategory(@Path("categoryId") Long categoryId); // Sửa từ @Query thành @Path
}
