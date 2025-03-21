package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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
import com.example.app.Model.ResponseBodyPaging;
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
    TextView tvCategoryInfo;
    TextView tvName;
    Long selectedCategoryId;
    String selectedCategoryName;
    private boolean isLoading = false;
    boolean isLoadedNewCategory = true;
    ProgressBar progressBar;
    int quantity;
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
        rcProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null
                        && linearLayoutManager.findLastCompletelyVisibleItemPosition() == productList.size() - 1
                        && !isLoading) {
                    isLoading = true;  // Chặn double call
                    isLoadedNewCategory = false;
                    GetProductByCategory(selectedCategoryId, selectedCategoryName);
                    Log.d("ero", "onScrolled: hello");
                }
            }
        });
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
        tvCategoryInfo = findViewById(R.id.txt_nameCate);
        rcProduct = findViewById(R.id.recyclerLastProducts);//Nguyễn Hữu Vinh 22110458
        progressBar = findViewById(R.id.progressBar);
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
                            selectedCategoryId = category.getId();
                            selectedCategoryName = category.getName();
                            isLoadedNewCategory = true;
                            // Khi bấm vào danh mục, gọi API lấy sản phẩm theo danh mục
                            GetProductByCategory(category.getId(),category.getName());
                            Log.d("ero", "onCategoryClick: hello");
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
    /*private void GetProductByCategory(Long categoryId, String categoryName) {
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getProductByCategory(categoryId).enqueue(new Callback<ResponseBodyPaging>() {
            @Override
            public void onResponse(Call<ResponseBodyPaging> call, Response<ResponseBodyPaging> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear(); // Xóa danh sách cũ

                    // Lấy danh sách sản phẩm từ response
                    List<Product> products = response.body().getProducts();
                    if (products != null) {
                        productList.addAll(products); // Thêm sản phẩm mới
                        quantity = productList.size();
                        productAdapter.notifyDataSetChanged();

                        String categoryInfo = categoryName + ": " + productList.size();
                        tvCategoryInfo.setText(categoryInfo);
                    } else {
                        Toast.makeText(MainActivity.this, "Không có sản phẩm nào trong danh mục này", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Không có sản phẩm nào trong danh mục này", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBodyPaging> call, Throwable t) {
                Log.d("API ERROR", "Lỗi khi gọi API lấy sản phẩm theo danh mục: " + t.getMessage());
            }
        });
    }*/
    private int currentPage = 0;

    private int totalPages = 1; // Cập nhật khi nhận được từ API

    private void GetProductByCategory(Long categoryId, String categoryName) {
        progressBar.setVisibility(View.VISIBLE);  // Hiển thị ProgressBar
        isLoading = true;  // Đánh dấu đang load dữ liệu

        if (isLoadedNewCategory) {
            currentPage = 0;   // Reset về trang đầu nếu là danh mục mới
            totalPages = 1;
            productList.clear();
            productAdapter.notifyDataSetChanged();
        }

        //Log.d("ero", "GetProductByCategory: isLoadedNewCategory = " + isLoadedNewCategory);

        // Nếu đã load hết trang thì không load nữa
        if (currentPage >= totalPages) {
            progressBar.setVisibility(View.GONE);
            isLoading = false;
            return;
        }

        apiService = RetrofitClient.getClient().create(ServiceAPI.class);

        // Tạo delay 1 giây cho cảm giác loading mượt hơn
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            apiService.getProductByCategory(categoryId, currentPage, 2).enqueue(new Callback<ResponseBodyPaging>() {
                @Override
                public void onResponse(Call<ResponseBodyPaging> call, Response<ResponseBodyPaging> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        ResponseBodyPaging body = response.body();
                        totalPages = body.getTotalPages();  // Cập nhật tổng số trang

                        List<Product> products = body.getProducts();
                        if (products != null && !products.isEmpty()) {
                            productList.addAll(products);  // Thêm sản phẩm vào danh sách
                            productAdapter.notifyDataSetChanged();

                            String categoryInfo = categoryName + ": " + productList.size();
                            tvCategoryInfo.setText(categoryInfo);

                            currentPage++;  // Tăng trang để load tiếp
                        } else if (productList.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Không có sản phẩm nào trong danh mục này", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Không có sản phẩm nào trong danh mục này", Toast.LENGTH_SHORT).show();
                    }

                    isLoading = false;
                    progressBar.setVisibility(View.GONE);  // Ẩn ProgressBar khi xong
                }

                @Override
                public void onFailure(Call<ResponseBodyPaging> call, Throwable t) {
                    Log.d("API ERROR", "Lỗi khi gọi API: " + t.getMessage());
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);  // Ẩn ProgressBar khi lỗi
                    Toast.makeText(MainActivity.this, "Lỗi kết nối. Vui lòng thử lại!", Toast.LENGTH_SHORT).show();
                }
            });
        }, 1000);  // Delay 1 giây (1000ms)
    }


}