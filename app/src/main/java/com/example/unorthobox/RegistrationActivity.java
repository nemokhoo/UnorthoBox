package com.example.unorthobox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class RegistrationActivity extends AppCompatActivity {

    ImageView submitButton, backButton;
    EditText emailText, passwordText,boxIdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        submitButton = findViewById(R.id.registrationSubmit);
        emailText = findViewById(R.id.registrationEmail);
        passwordText = findViewById(R.id.registrationPassword);
        boxIdText = findViewById(R.id.registrationBoxID);
        backButton = findViewById(R.id.registrationBack);
        backButton.setOnClickListener(view -> finish());
        submitButton.setOnClickListener(view -> toLogin());

    }

    private void toLogin(){
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }
}