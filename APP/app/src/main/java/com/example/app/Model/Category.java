//Nguyễn Phan Minh Trí - 22110443
package com.example.app.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
//Nguyễn Phan Minh Trí - 22110443
public class Category implements Serializable {
    @SerializedName("categoryId")
    private Long id;
    @SerializedName("categoryName")
    private String name;
    @SerializedName("image")
    private String images;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

}
