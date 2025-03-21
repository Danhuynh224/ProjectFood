package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.API.RetrofitClient;
import com.example.app.API.ServiceAPI;
import com.example.app.Model.Category;
import com.example.app.Model.Product;
import com.example.app.adapter.CategoryAdapter;
import com.example.app.adapter.ProductAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
// Ho Nhut Tan - 22110412
// Nguyễn Phan Minh Trí - 22110443
public class MainActivity extends AppCompatActivity {

    RecyclerView rcCate;
    CategoryAdapter categoryAdapter;
    ServiceAPI apiService;
    List<Category> categoryList;
    List<Product> productList;
    RecyclerView rcProduct;
    ProductAdapter productAdapter;
    SharedPreferences sharedPreferences;
    TextView tvName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AnhXa();
        GetCategory(); // Nguyễn Phan Minh Trí - 22110443
        GetProduct();  // Nguyễn Hữu VInh - 22110458
        // Ho Nhut Tan - 22110412
        sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);

        tvName.setText("Hi, " + sharedPreferences.getString("username", ""));

        ImageButton btnLogout = findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(view -> {
            // Lấy đúng file SharedPreferences đang lưu login
            SharedPreferences sharedPreferences = getSharedPreferences("LoginDetails", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Xóa toàn bộ dữ liệu login
            editor.apply(); // Lưu thay đổi
            Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
            // Quay về IntroActivity
            Intent intent = new Intent(this, IntroActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear hết backstack
            startActivity(intent);
            finish(); // Đóng Activity hiện tại
        });

    }

    private void AnhXa() {
        tvName = findViewById(R.id.tvName);
        rcCate = findViewById(R.id.recyclerCategories);
        rcProduct = findViewById(R.id.recyclerLastProducts);//Nguyễn Hữu Vinh 22110458
    }

    private void GetCategory() {
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getCategoriesAll().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if (response.isSuccessful()) {
                    categoryList = response.body();
                    categoryAdapter = new CategoryAdapter(MainActivity.this, categoryList, new CategoryAdapter.OnCategoryClickListener() {
                        @Override
                        public void onCategoryClick(Category category) {
                            // Khi bấm vào danh mục, gọi API lấy sản phẩm theo danh mục
                            GetProductByCategory(category.getId());
                        }
                    });

                    rcCate.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                    rcCate.setLayoutManager(layoutManager);
                    rcCate.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    Log.d("API ERROR", "Không thể lấy danh mục");
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d("API ERROR", "Lỗi khi gọi API danh mục: " + t.getMessage());
            }
        });
    }
    private void GetProduct() {//Hàm lấy sản phẩm mới nhất Nguyễn Hữu Vinh 22110458
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getLastProducts().enqueue(new Callback<List<Product>>(){
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()) {
                    productList = response.body();
                    productAdapter = new ProductAdapter(MainActivity.this,productList);
                    rcProduct.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    rcProduct.setLayoutManager(layoutManager);
                    rcProduct.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();

                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("logg product", t.getMessage());
            }
        });
    }
    // Hàm lấy sản phẩm theo danh mục Nguyễn Hữu Vinh 22110458
    private void GetProductByCategory(Long categoryId) {
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getProductByCategory(categoryId).enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear(); // Xóa danh sách cũ
                    productList.addAll(response.body()); // Thêm sản phẩm mới
                    productAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Không có sản phẩm nào trong danh mục này", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("API ERROR", "Lỗi khi gọi API lấy sản phẩm theo danh mục: " + t.getMessage());
            }
        });
    }
}