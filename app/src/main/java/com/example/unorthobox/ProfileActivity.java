package com.example.unorthobox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    EditText emailText, passwordText, boxIdText;
    ImageView editButton, backButton, profileChange, newMessageButton, userButton, homeButton;;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        emailText = findViewById(R.id.editEmail);
        passwordText = findViewById(R.id.editPassword);
        boxIdText = findViewById(R.id.editBoxID);
        backButton = findViewById(R.id.editBack);


        newMessageButton.setOnClickListener(view -> toNotifications());
        userButton.setOnClickListener(view -> toUser());
        homeButton.setOnClickListener(view -> toHome());

        backButton.setOnClickListener(view -> finish());

        editButton = findViewById(R.id.editButton);
        profileChange = findViewById(R.id.profileChange);

        editButton.setOnClickListener(view -> editInfo());
        profileChange.setOnClickListener(view -> changePicture());
    }

    private void editInfo(){
        Context context = getApplicationContext();
        CharSequence text = "Info edited!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void changePicture(){
        Context context = getApplicationContext();
        CharSequence text = "Feature not implemented yet :((";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void toNotifications(){
        Intent switchActivityIntent = new Intent(this, NotificationActivity.class);
        startActivity(switchActivityIntent);
    }
    private void toHome(){
        Intent switchActivityIntent = new Intent(this, HomeActivity.class);
        startActivity(switchActivityIntent);
    }
    private void toUser(){
        Context context = getApplicationContext();
        CharSequence text = "You are already at profile page!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}