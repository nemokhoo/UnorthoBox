package com.example.unorthobox;

public interface RegistrationCallback {
    void onSuccess(String role, String inputId);

    void onFailure();
}
