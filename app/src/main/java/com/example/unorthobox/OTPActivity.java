package com.example.unorthobox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class OTPActivity extends AppCompatActivity {

    ImageView submitButton, generateButton, backButton, newMessageButton, userButton, homeButton;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth;

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
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder otp= new StringBuilder();
        for(int i=0;i<10;i++){
            otp.append(secureRandom.nextInt(10));
        }
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, otp.toString(), duration);
        toast.show();
        String userId = "yourUserId";
        String boxId = "yourBoxId";
        long expiresIn = 10 * 60 * 1000; // Expires in 10 minutes

        saveOtpToFirebase(otp.toString(), userId, boxId, expiresIn);
    }
    public void saveOtpToFirebase(String otp, String userId, String boxId, long expiresIn) {
        DatabaseReference otpsRef = database.child("otps");
        Query otpQuery = otpsRef.orderByChild("userId").equalTo(userId);

        otpQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean otpExists = false;

                for (DataSnapshot otpSnapshot : dataSnapshot.getChildren()) {
                    String snapshotBoxId = otpSnapshot.child("boxId").getValue(String.class);
                    if (boxId.equals(snapshotBoxId)) {
                        otpExists = true;
                        break;
                    }
                }

                if (!otpExists) {
                    // Create a new unique key for this OTP entry
                    DatabaseReference otpRef = otpsRef.push();

                    // Create a map to store the OTP data
                    Map<String, Object> otpData = new HashMap<>();
                    otpData.put("otp", otp);
                    otpData.put("userId", userId);
                    otpData.put("boxId", boxId);
                    otpData.put("expiresAt", System.currentTimeMillis() + expiresIn);

                    // Save the OTP data to the Realtime Database
                    otpRef.setValue(otpData);
                } else {
                    // Handle the case when an OTP already exists
                    // You may show an error message or delete the existing OTP and save a new one
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors here
            }
        });
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