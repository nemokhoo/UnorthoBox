package com.example.unorthobox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {

    ImageView loginButton, registrationButton;
    Button otpButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_page);

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(view -> toLogin());
        otpButton=findViewById(R.id.otp_button);
        otpButton.setOnClickListener(view -> toOTP());
        registrationButton = findViewById(R.id.registerButton);
        registrationButton.setOnClickListener(view -> toRegister());

    }
    private void toOTP() {
        Intent switchActivityIntent = new Intent(this, SubmitOTPActivity.class);
        startActivity(switchActivityIntent);
    }

    private void toLogin() {
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }

    private void toRegister() {
        Intent switchActivityIntent = new Intent(this, RegistrationActivity.class);
        startActivity(switchActivityIntent);
    }


}