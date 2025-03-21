//Huỳnh Việt Đan - 22110306
package com.example.app.API;

import com.example.app.Model.ApiResponse;
import com.example.app.Model.ForgetRequest;
import com.example.app.Model.OTPRequest;
import com.example.app.Model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthAPI {
    @POST("/api/auth/register")
    Call<ApiResponse> register(@Body User user);

    @POST("/api/auth/login")
    Call<ApiResponse> login(@Body User user);

    @POST("/api/auth/forget")
    Call<ApiResponse> forget(@Body ForgetRequest request);

    @POST("/api/auth/active")
    Call<ApiResponse> activate(@Body OTPRequest request);
}
