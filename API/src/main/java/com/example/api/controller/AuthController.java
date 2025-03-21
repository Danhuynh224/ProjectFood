// Huỳnh Việt Đan -22110306

package com.example.api.controller;

import com.example.api.entity.User;
import com.example.api.request.ForgetRequest;
import com.example.api.request.OTPRequest;
import com.example.api.request.ResetPassRequest;
import com.example.api.service.AuthService;
import com.example.api.service.OTPService;
import com.example.api.utils.ErrorResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private OTPService otpService;
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody User user) {
        user = authService.registerUser(user);
        if (user == null) {
            ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                    "User already exists",
                    "A user with this email already exists in the system.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        otpService.sendOTPToEmail(user.getEmail());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Registration successful, OTP sent to email");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody User user) {
        System.out.println(user.toString());
        User loginUser = authService.login(user);

        if (loginUser==null) {
            ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                    "Login unsuccessful",
                    "Email or password is incorrect.");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(loginUser, HttpStatus.CREATED);
    }
    @PostMapping("/active")
    public ResponseEntity<?> active(@Valid @RequestBody OTPRequest otpRequest) {
        if(authService.activeUser(otpRequest))
        {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Your account is active");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "Active unsuccessful",
                "Your OTP is incorrect.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/forget")
    public ResponseEntity<?> forget(@Valid @RequestBody ForgetRequest forgetRequest) {
        authService.forget(forgetRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Please check your email");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("reset")
    public ResponseEntity<?> reset(@Valid @RequestBody ResetPassRequest resetPassRequest) {
        if(authService.resetPass(resetPassRequest)){
            Map<String, String> response = new HashMap<>();
            response.put("message", "New password has been reset");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ErrorResponse error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),
                "Reset unsuccessful",
                "Your password is incorrect.");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}