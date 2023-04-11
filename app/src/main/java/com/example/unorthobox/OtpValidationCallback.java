package com.example.unorthobox;

public interface OtpValidationCallback {
    void onOtpValid(String userId);
    void onOtpInvalidOrExpired();
}
