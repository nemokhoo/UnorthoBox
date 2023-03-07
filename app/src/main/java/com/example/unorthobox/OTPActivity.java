package com.example.unorthobox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class OTPActivity extends AppCompatActivity {

    ImageView submitButton, generateButton, backButton, newMessageButton, userButton, homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submitotp_page);

        newMessageButton = findViewById(R.id.otpNewMessage);
        userButton = findViewById(R.id.otpUser);
        homeButton = findViewById(R.id.otpHome);

        newMessageButton.setOnClickListener(view -> toMessages());
        userButton.setOnClickListener(view -> toUser());
        homeButton.setOnClickListener(view -> toHome());

        submitButton = findViewById(R.id.submitOTPButton);
        generateButton = findViewById(R.id.generateOTPButton);
        backButton = findViewById(R.id.otpBack);

        submitButton.setOnClickListener(view -> submit());
        generateButton.setOnClickListener(view -> generateOTP() );
        backButton.setOnClickListener(view -> finish());

    }

    private void submit(){
        Context context = getApplicationContext();
        CharSequence text = "Unlocked!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void generateOTP(){
        Context context = getApplicationContext();
        CharSequence text = "Your OTP is 12345";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void toMessages(){
        Intent switchActivityIntent = new Intent(this, NotificationActivity.class);
        startActivity(switchActivityIntent);
    }
    private void toHome(){
        Intent switchActivityIntent = new Intent(this, HomeActivity.class);
        startActivity(switchActivityIntent);
    }
    private void toUser(){
        Intent switchActivityIntent = new Intent(this, ProfileActivity.class);
        startActivity(switchActivityIntent);
    }
}