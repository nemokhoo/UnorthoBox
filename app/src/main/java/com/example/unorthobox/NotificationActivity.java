package com.example.unorthobox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class NotificationActivity extends AppCompatActivity {
    ImageView newMessageButton, userButton, homeButton, chat, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_page);

        newMessageButton = findViewById(R.id.notificationNewMessage);
        userButton = findViewById(R.id.notificationUser);
        homeButton = findViewById(R.id.notificationHome);
        backButton = findViewById(R.id.notificationBack);
        chat = findViewById(R.id.samplePlaceholder);

        newMessageButton.setOnClickListener(view -> toNotifications());
        userButton.setOnClickListener(view -> toUser());
        homeButton.setOnClickListener(view -> toHome());
        backButton.setOnClickListener(view -> finish());
        chat.setOnClickListener(view -> toMessage());

    }

    private void toMessage(){
        Intent switchActivityIntent = new Intent(this, MessageActivity.class);
        startActivity(switchActivityIntent);
    }

    private void toNotifications(){
        Context context = getApplicationContext();
        CharSequence text = "You are already at notifications!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
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