package com.lexso.login.model;

public class ModelUser {
    private int userId;
    private String username;
    // Add other fields as needed (e.g., name, role)

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}