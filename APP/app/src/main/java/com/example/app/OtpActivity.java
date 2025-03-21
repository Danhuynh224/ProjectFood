package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.app.API.AuthAPI;
import com.example.app.API.RetrofitClient;
import com.example.app.Model.ApiResponse;
import com.example.app.Model.OTPRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//22110408 _ Nguyễn Hồng Sơn
public class OtpActivity extends AppCompatActivity {
    TextView email;
    ImageButton active;
    AuthAPI authAPI;
    EditText otp1, otp2, otp3, otp4, otp5, otp6;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        String userName = getIntent().getStringExtra("NAME");
        String userPass = getIntent().getStringExtra("PASSWORD");

        email = findViewById(R.id.tv_email);
        active = findViewById(R.id.btn_active);
        email.setText(userEmail);

        otp1 = findViewById(R.id.otp_input_1);
        otp2 = findViewById(R.id.otp_input_2);
        otp3 = findViewById(R.id.otp_input_3);
        otp4 = findViewById(R.id.otp_input_4);
        otp5 = findViewById(R.id.otp_input_5);
        otp6 = findViewById(R.id.otp_input_6);

        setupOtpAutoMove(otp1, otp2);
        setupOtpAutoMove(otp2, otp3);
        setupOtpAutoMove(otp3, otp4);
        setupOtpAutoMove(otp4, otp5);
        setupOtpAutoMove(otp5, otp6);
        setupOtpAutoMove(otp6, null);

        setupOtpBackspace(otp2, otp1);
        setupOtpBackspace(otp3, otp2);
        setupOtpBackspace(otp4, otp3);
        setupOtpBackspace(otp5, otp4);
        setupOtpBackspace(otp6, otp5);

        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpCode = getOtpCode();
                if (otpCode.length() == 6) {
                    Toast.makeText(OtpActivity.this, "Mã OTP: " + otpCode, Toast.LENGTH_SHORT).show();
                    active_user(userEmail, otpCode);
                } else {
                    Toast.makeText(OtpActivity.this, "Vui lòng nhập đủ 6 số OTP!", Toast.LENGTH_SHORT).show();
                }
            }

            private void active_user(String email, String otpCode) {
                OTPRequest otpRequest = new OTPRequest();
                otpRequest.setOtp(otpCode);
                otpRequest.setEmail(email);
                authAPI = RetrofitClient.getClient().create(AuthAPI.class);
                authAPI.activate(otpRequest).enqueue(new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(OtpActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
                            intent.putExtra("NAME", userName);
                            intent.putExtra("PASSWORD",userPass);
                            startActivity(intent);
                        } else {
                            try {
                                // Đọc lỗi từ errorBody()
                                String errorBody = response.errorBody().string();
                                Log.e("API_ERROR", "Response error: " + errorBody);
                                Toast.makeText(OtpActivity.this, errorBody, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Log.e("API_ERROR", "Error reading error body", e);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(OtpActivity.this, "Lỗi " + t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.d("Lỗi", t.getMessage());
                    }
                });

            }

            private String getOtpCode() {
                return otp1.getText().toString() +
                        otp2.getText().toString() +
                        otp3.getText().toString() +
                        otp4.getText().toString() +
                        otp5.getText().toString() +
                        otp6.getText().toString();
            }
        });


    }

    private void setupOtpBackspace(EditText current, EditText previous) {
        current.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && current.getText().length() == 0) {
                    if (previous != null) {
                        previous.requestFocus(); // Quay lại ô trước
                    }
                    return true;
                } else if (keyCode == KeyEvent.KEYCODE_DEL && current.getText().length() != 0) {
                    current.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    private void setupOtpAutoMove(EditText current, EditText next) {
        current.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1 && next != null) {
                    next.requestFocus(); // Chuyển đến ô tiếp theo
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
