// Huỳnh Việt Đan -22110306
package com.example.api.entity;

import jakarta.persistence.Entity;
import lombok.*;
import org.springframework.data.annotation.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @jakarta.persistence.Id
    @Id
    private String email;
    private String password;
    private String name;
    private int gender; //0 là nữ , 1 là nam
    private boolean isActive;

}
