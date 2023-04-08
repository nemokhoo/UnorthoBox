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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText emailText, passwordText;
    ImageView submitButton, backButton;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        emailText = findViewById(R.id.loginEmail);
        passwordText = findViewById(R.id.loginPassword);
        submitButton = findViewById(R.id.loginSubmit);
        backButton = findViewById(R.id.loginBack);
        //submitButton.setOnClickListener(view -> toHome());
        backButton.setOnClickListener(view -> toMainPage());
        auth = FirebaseAuth.getInstance();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = emailText.getText().toString();
                String txt_password = passwordText.getText().toString();
                if(TextUtils.isEmpty(txt_email)){
                    Toast.makeText(LoginActivity.this, "You did not enter email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(txt_password)){
                    Toast.makeText(LoginActivity.this, "You did not enter password", Toast.LENGTH_SHORT).show();
                    return;
                }
                loginUser(txt_email,txt_password);
            }
        });
    }
    private void getUserRole(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    System.out.println(user.boxID);
                    navigateToRoleSpecificActivity(user.boxID);
                } else {
                    // Handle error (user not found in the database)
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });
    }
    private void navigateToRoleSpecificActivity(String role) {
        Intent intent;
        System.out.println(role);
        if ("user".equals(role)) {
            intent = new Intent(this, HomeActivity.class);
        } else if ("delivery".equals(role)) {
            intent = new Intent(this, DeliverActivity.class);
        } else {
            // Handle error (unknown role)
            return;
        }

        startActivity(intent);
        finish(); // Remove the current activity from the back stack
    }
    private void toMainPage(){
        Intent switchActivityIntent = new Intent(this, StartActivity.class);
        startActivity(switchActivityIntent);
    }
    private void loginUser(String email, String password) {

        if(email!=null && password!=null){
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser user = auth.getCurrentUser();
                        Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        getUserRole(user.getUid());
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

    }

    public void updateUI(FirebaseUser currentUser) {
        Intent HomeIntent = new Intent(getApplicationContext(), HomeActivity.class);
        HomeIntent.putExtra("email", currentUser.getEmail());
        System.out.println("email: " + currentUser.getEmail());
        startActivity(HomeIntent);
    }
}
