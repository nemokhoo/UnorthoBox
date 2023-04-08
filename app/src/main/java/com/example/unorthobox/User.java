package com.example.unorthobox;

public class User {

    public String boxID, email, userId;

    public User() {

    }

    public User(String UserId, String email, String boxID) {
        this.boxID = boxID;
        this.email = email;
        this.userId= UserId;
    }
}
