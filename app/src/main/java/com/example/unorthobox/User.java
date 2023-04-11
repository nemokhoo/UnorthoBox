package com.example.unorthobox;

public class User {

    public String boxID, email, userId, role;

    public User() {

    }

    public User(String UserId, String email, String boxID, String role) {
        this.boxID = boxID;
        this.role = role;
        this.email = email;
        this.userId= UserId;
    }
}
