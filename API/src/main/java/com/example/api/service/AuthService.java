// Huỳnh Việt Đan -22110306
package com.example.api.service;

import com.example.api.entity.OTP;
import com.example.api.entity.User;
import com.example.api.repository.OTPRepository;
import com.example.api.repository.UserRepository;
import com.example.api.request.ForgetRequest;
import com.example.api.request.LoginRequest;
import com.example.api.request.OTPRequest;
import com.example.api.request.ResetPassRequest;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    OTPRepository otpRepository;
    @Autowired
    private JavaMailSender emailSender;
    public User registerUser(User user) {
        User existingUser = userRepository.findByName(user.getName());
        if (existingUser != null) {
            return null;
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setActive(false); // Chưa kích hoạt
        userRepository.save(user);
        return user;
    }

    public User login(LoginRequest user) {
        User existingUser = userRepository.findByName(user.getName());
        if (existingUser == null || !BCrypt.checkpw(user.getPassword(), existingUser.getPassword()) || !existingUser.isActive()){
            return null;
        }
        return existingUser;
    }

    public boolean activeUser(OTPRequest otpRequest) {
        User user = userRepository.findByEmail(otpRequest.getEmail());
        OTP otp = otpRepository.findByEmail(otpRequest.getEmail());
        if (user != null && otp != null && !user.isActive()) {
            if (otpRequest.getOtp().equals(otp.getValue())) {
                user.setActive(true);
                userRepository.save(user);
                otpRepository.delete(otp);
                return true;
            }
        }
        return false;
    }


    public void forget(ForgetRequest forgetRequest) {
        User existingUser = userRepository.findByEmail(forgetRequest.getEmail());

        if (existingUser != null) {
            String newpass = UUID.randomUUID().toString().replace("-", "");
            sendPassToEmail(existingUser.getEmail(),  newpass );
            existingUser.setPassword(BCrypt.hashpw(newpass, BCrypt.gensalt()));
            userRepository.save(existingUser);
            System.out.println(newpass);
        }
    }


    public boolean resetPass(ResetPassRequest resetPassRequest) {
        User existingUser = userRepository.findByEmail(resetPassRequest.getEmail());
        if (existingUser != null && BCrypt.checkpw(resetPassRequest.getOldPass(), existingUser.getPassword())) {
            existingUser.setPassword(BCrypt.hashpw(resetPassRequest.getNewPass(), BCrypt.gensalt()));
            userRepository.save(existingUser);
            return true;
        }
        return false;
    }

    public void sendPassToEmail(String email, String pass) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your new password");
        message.setText("Your new password is: " + pass);
        emailSender.send(message);
    }
}
