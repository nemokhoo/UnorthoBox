package com.example.unorthobox;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends AppCompatActivity{

    ImageView submitButton, backButton;
    EditText emailText, passwordText,boxIdText;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();



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
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                determineRoleAndRegister(boxIdText.getText().toString());
//                String txt_email = emailText.getText().toString();
//                String txt_password = passwordText.getText().toString();
//                String txt_boxID = boxIdText.getText().toString();
//
//                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_boxID)) {
//                    Toast.makeText(RegistrationActivity.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
//                } else if (txt_password.length() < 6) {
//                    Toast.makeText(RegistrationActivity.this, "Password too short", Toast.LENGTH_SHORT).show();
//                } else {
//                    determineRoleAndRegister(txt_email, txt_password, txt_boxID);
                //}
            };
        });

    }

    private void registerUser(String role,String inputId, RegistrationCallback callback) {
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Registration successful, store user data and role in the database
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid().toString(), user.getEmail().toString(), role, inputId);

                            // Invoke the success callback
                            callback.onSuccess(role, inputId);
                        } else {
                            // If registration fails, display a message to the user
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();

                            // Invoke the failure callback
                            callback.onFailure();
                        }
                    }
                });
    }

    private void writeNewUser(String userId, String email, String role, String inputId) {
        User user = new User(userId,email, role);
        DatabaseReference userRef = mDatabase.child("users").child(userId);

        userRef.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Update the boxId or deliveryId to false after successfully writing the user data
                    if (role.equals("user")) {
                        FirebaseDatabase.getInstance().getReference().child("boxIds").child(inputId).setValue(false);
                    } else if (role.equals("delivery")) {
                        FirebaseDatabase.getInstance().getReference().child("deliveryIds").child(inputId).setValue(false);
                    }
                    // Navigate to the next activity or show a success message
                } else {
                    // Handle error
                }
            }
        });
    }
    private void determineRoleAndRegister(String inputId) {
        DatabaseReference boxIdRef = FirebaseDatabase.getInstance().getReference().child("boxIds");
        DatabaseReference deliveryIdRef = FirebaseDatabase.getInstance().getReference().child("deliveryIds");
        RegistrationCallback registrationCallback = new RegistrationCallback() {
            @Override
            public void onSuccess(String role, String inputId) {
                Toast.makeText(RegistrationActivity.this, "Registration success.",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure() {
                // Handle registration failure
                Toast.makeText(RegistrationActivity.this, "Registration failed.",
                        Toast.LENGTH_SHORT).show();                   }
        };
        boxIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(inputId)  && Boolean.TRUE.equals(dataSnapshot.child(inputId).getValue(Boolean.class))) {
                    // Proceed with the user registration with the "user" role
                    registerUser("user", inputId, registrationCallback);

                } else {
                    // If the input is not a valid box ID, check for valid delivery IDs
                    deliveryIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(inputId) && Boolean.TRUE.equals(dataSnapshot.child(inputId).getValue(Boolean.class))) {
                                // Proceed with the user registration with the "delivery" role
                                registerUser("delivery", inputId, registrationCallback);


                            } else {
                                // Handle invalid input or failed registration
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void toLogin(){
        Intent switchActivityIntent = new Intent(this, LoginActivity.class);
        startActivity(switchActivityIntent);
    }
}