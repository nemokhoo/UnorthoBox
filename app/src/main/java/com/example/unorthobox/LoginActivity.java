package com.example.unorthobox;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

public class LoginActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    ImageView submitButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        emailText = findViewById(R.id.loginEmail);
        passwordText = findViewById(R.id.loginPassword);
        submitButton = findViewById(R.id.loginSubmit);
        backButton = findViewById(R.id.loginBack);
        submitButton.setOnClickListener(view -> toHome());
        backButton.setOnClickListener(view -> finish());
    }

    private void toHome(){
        Intent switchActivityIntent = new Intent(this, HomeActivity.class);
        startActivity(switchActivityIntent);
    }
}