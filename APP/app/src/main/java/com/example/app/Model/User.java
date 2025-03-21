//Huỳnh Việt Đan - 22110306
package com.example.app.Model;

public class User {
    private String name;
    private int gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public User(String name, int gender, String email, String password, boolean active) {
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.active = active;
    }

    public User() {
    }

    private String email;
    private String password;
    private boolean active = false;

    public User(String email, String password, boolean active) {
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
