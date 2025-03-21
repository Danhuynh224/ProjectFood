package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.API.AuthAPI;
import com.example.app.API.RetrofitClient;
import com.example.app.Model.ApiResponse;
import com.example.app.Model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//22110408 _ Nguyễn Hồng Sơn
public class SignupActivity extends AppCompatActivity {
    EditText email,name, password, confpass;
    ImageButton signup;
    RadioGroup genderGroup;
    RadioButton selectedGenderButton;
    AuthAPI authAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name = findViewById(R.id.tv_name);
        email = findViewById(R.id.tv_email);
        password = findViewById(R.id.tv_pass);
        signup = findViewById(R.id.btn_signup);
        confpass = findViewById(R.id.tv_passConf);
        genderGroup = findViewById(R.id.genderGroup);


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = password.getText().toString();
                String passAgain = confpass.getText().toString();

                // Kiểm tra mật khẩu có khớp hay không
                if (!pass.equals(passAgain)) {
                    Toast.makeText(SignupActivity.this, "Mật khẩu nhập không khớp nhau", Toast.LENGTH_SHORT).show();
                } else {
                    // Lấy ID của RadioButton được chọn
                    int selectedId = genderGroup.getCheckedRadioButtonId();
                    if (selectedId == -1) {
                        Toast.makeText(SignupActivity.this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // Lấy text của RadioButton được chọn
                    selectedGenderButton = findViewById(selectedId);
                    String gender = selectedGenderButton.getText().toString();

                    User user = new User();
                    user.setName(name.getText().toString());
                    user.setEmail(email.getText().toString());
                    user.setPassword(pass);
                    if(gender.equals("male")){
                        user.setGender(0);
                    } else {
                        user.setGender(1);
                    }
                    // Gọi hàm đăng ký người dùng
                    registerUser(user);
                }
            }

            private void registerUser(User user) {
                authAPI = RetrofitClient.getClient().create(AuthAPI.class);
                authAPI.register(user).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(SignupActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            // Chuyển sang OTPActivity và truyền dữ liệu
                            Intent intent = new Intent(SignupActivity.this, OtpActivity.class);
                            intent.putExtra("USER_EMAIL", user.getEmail());
                            startActivity(intent);
                        } else {
                            try {
                                // Đọc lỗi từ errorBody()
                                String errorBody = response.errorBody().string();
                                Log.e("API_ERROR", "Response error: " + errorBody);
                                Toast.makeText(SignupActivity.this, "Invalid email and username", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.e("API_ERROR", "Error reading error body", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(SignupActivity.this, "Lỗi " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Lỗi", t.getMessage());
                    }
                });

            }
        });
    }
}
