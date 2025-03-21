//Nguyễn Phan Minh Trí - 22110443
package com.example.app.API;

import com.example.app.Model.Category;
import com.example.app.Model.Product;
import com.example.app.Model.ResponseBodyPaging;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ServiceAPI {
    @GET("/api/category/all-categories")
    Call<List<Category>> getCategoriesAll();
    @GET("/api/product/last-products")
    Call<List<Product>> getLastProducts();
    @GET("/api/product/paging")
    Call<ResponseBodyPaging> getProductsPaging(
            @Query("page") int page,
            @Query("size") int size
    );
}
