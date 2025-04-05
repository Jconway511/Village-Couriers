package com.example.villagecouriers;

public class User {
    private int id;
    private String name;
    private String email;
    private String userType;
    private String password;

    public User(int userId, String name, String email, String password, String userType) {
        this.id = userId;
        this.name = name;
        this.email = email;
        this.userType = userType;
        this.password = password;
    }


    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getUserType() {
        return userType;
    }

    public String toString() {
        return "Name: " + name + " (" + email + ")";
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }
}