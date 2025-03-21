// Huỳnh Việt Đan -22110306
package com.example.api.repository;


import com.example.api.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTP, String> {
     OTP findByEmail(String email);
}
