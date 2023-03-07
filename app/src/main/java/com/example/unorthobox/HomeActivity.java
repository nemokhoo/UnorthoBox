package com.example.unorthobox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;



public class HomeActivity extends AppCompatActivity {

    ImageView lockButton, OTPButton, backButton, newMessageButton, userButton, homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        lockButton = findViewById(R.id.lockButton);
        OTPButton = findViewById(R.id.otpButton);
        backButton = findViewById(R.id.homeBack);

        newMessageButton = findViewById(R.id.homeNewMessage);
        userButton = findViewById(R.id.homeUser);
        homeButton = findViewById(R.id.homeHome);

        lockButton.setOnClickListener(view -> lock());
        backButton.setOnClickListener(view -> finish());
        OTPButton.setOnClickListener(view -> toOTP());

        newMessageButton.setOnClickListener(view -> toNotifications());
        userButton.setOnClickListener(view -> toUser());
        homeButton.setOnClickListener(view -> toHome());

    }

    private void lock(){
        Context context = getApplicationContext();
        CharSequence text = "Locked!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void toOTP(){
        Intent switchActivityIntent = new Intent(this, OTPActivity.class);
        startActivity(switchActivityIntent);
    }

    private void toNotifications(){
        Intent switchActivityIntent = new Intent(this, NotificationActivity.class);
        startActivity(switchActivityIntent);
    }
    private void toHome(){
        Context context = getApplicationContext();
        CharSequence text = "You are already home!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    private void toUser(){
        Intent switchActivityIntent = new Intent(this, ProfileActivity.class);
        startActivity(switchActivityIntent);
    }

}