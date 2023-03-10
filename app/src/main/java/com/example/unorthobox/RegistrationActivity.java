package com.example.unorthobox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    ImageView submitButton, backButton;
    EditText emailText, passwordText,boxIdText;
    FirebaseAuth auth = FirebaseAuth.getInstance();


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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_email = emailText.getText().toString();
                String txt_password = passwordText.getText().toString();
                String txt_boxID = boxIdText.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_boxID)) {
                    Toast.makeText(RegistrationActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegistrationActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email, txt_password, txt_boxID);
                }
            }
        });

    }

    private void registerUser(String email, String password, String boxID) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    User user = new User(
                            boxID,
                            email
                    );
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegistrationActivity.this, "Registering User Successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegistrationActivity.this, StartActivity.class));

                                    } else {
                                        Toast.makeText(RegistrationActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void toLogin(){
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }
}