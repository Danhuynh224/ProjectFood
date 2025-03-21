<<<<<<< HEAD
<<<<<<< HEAD
//Huỳnh Tuấn Kiệt - 22110358
=======
>>>>>>> a9919c7f42d9d5ba1067beae7910aa5256e2e76f
=======
//Huỳnh Tuấn Kiệt - 22110358
>>>>>>> dev_dan
package com.example.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Categories")
public class Category implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;
    private String categoryName;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Set<Product> products;
}