package com.example.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
// Ho Nhut Tan - 22110412
// Nguyễn Phan Minh Trí - 22110443
public class MainActivity extends AppCompatActivity {
    private int currentPage = 0;
    private final int pageSize = 4;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private List<Product> productList = new ArrayList<>();
    //private ProductAdapter productAdapter;
    RecyclerView rcCate;
    CategoryAdapter categoryAdapter;
    ServiceAPI apiService;
    List<Category> categoryList;
    //List<Product> productList;
    RecyclerView rcProduct;
    ProductAdapter productAdapter;
    SharedPreferences sharedPreferences;
    TextView tvName;
    private ProgressBar progressBar;
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
        //GetProduct();
        loadProductsPaging();
        rcProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                Log.d("logg", "onScrolled: Hello");
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager != null && !isLoading && !isLastPage) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        // Kéo tới cuối -> Gọi tiếp API
                        loadProductsPaging();
                    }
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
        rcProduct = findViewById(R.id.recyclerLastProducts);//Nguyễn Hữu Vinh 22110458
        progressBar = findViewById(R.id.progressBar);
    }

    private void GetCategory() { // Nguyễn Phan Minh Trí - 22110443

        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getCategoriesAll().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()) {
                    categoryList = response.body();

                    categoryAdapter = new CategoryAdapter(MainActivity.this, categoryList);
                    rcCate.setHasFixedSize(true);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                            LinearLayoutManager.HORIZONTAL, false);
                    rcCate.setLayoutManager(layoutManager);
                    rcCate.setAdapter(categoryAdapter);
                    categoryAdapter.notifyDataSetChanged();
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Log.d("logg", t.getMessage());
            }
        });
    }
    private void GetProduct() {
        apiService = RetrofitClient.getClient().create(ServiceAPI.class);
        apiService.getLastProducts().enqueue(new Callback<List<Product>>(){
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if(response.isSuccessful()) {
                    productList = response.body();
                    productAdapter = new ProductAdapter(MainActivity.this, productList);
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
    private void loadProductsPaging() {
        isLoading = true;
        progressBar.setVisibility(View.VISIBLE); // Hiển thị ProgressBar (nếu có)

        new android.os.Handler().postDelayed(() -> {
            apiService = RetrofitClient.getClient().create(ServiceAPI.class);
            apiService.getProductsPaging(currentPage, pageSize).enqueue(new Callback<ResponseBodyPaging>() {
                @Override
                public void onResponse(Call<ResponseBodyPaging> call, Response<ResponseBodyPaging> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Product> newProducts = response.body().getProducts();
                        productList.addAll(newProducts);
                        if (productAdapter == null) {
                            productAdapter = new ProductAdapter(MainActivity.this, productList);
                            rcProduct.setHasFixedSize(true);
                            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
                            rcProduct.setLayoutManager(layoutManager);
                            rcProduct.setAdapter(productAdapter);
                        } else {
                            productAdapter.notifyDataSetChanged();
                        }
                        currentPage++;
                        if (currentPage >= response.body().getTotalPages()) {
                            isLastPage = true;
                        }
                    }
                    isLoading = false;
                    progressBar.setVisibility(View.GONE); // Ẩn ProgressBar sau khi load xong
                }

                @Override
                public void onFailure(Call<ResponseBodyPaging> call, Throwable t) {
                    Log.d("logg product", t.getMessage());
                    isLoading = false;
                    progressBar.setVisibility(View.GONE);
                }
            });
        }, 500); // Delay 1.5 giây (1500 milliseconds)
    }

}