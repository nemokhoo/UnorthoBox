package com.example.unorthobox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SubmitOTPActivity extends AppCompatActivity {
    private EditText otps;
    private Button submitOtp;
    private DatabaseReference otpReference;
    boolean lock_status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_otpactivity);
        otps = findViewById(R.id.otpInput);
        otpReference = FirebaseDatabase.getInstance().getReference("otps");

        submitOtp = findViewById(R.id.otpSubmitInput);
        submitOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSubmitOtp(otps.getText().toString());
            }
        });
    }
    private void lock(String topic) {
        Context context = this;
        CharSequence text = "Locked!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MqttConnection mqttInstance = MqttConnection.getInstance(context);
        if (lock_status) {
            mqttInstance.publish(topic, "unlock", context);
        } else {
            mqttInstance.publish(topic, "lock", context);
        }

        lock_status = !lock_status;
    }
    public void validateAndSubmitOtp(final String enteredOtp) {
        otpReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean otpValid = false;
                String validUserId = null;
                int enteredOtpInt = 0;

                try {
                    enteredOtpInt = Integer.parseInt(enteredOtp);
                } catch (NumberFormatException e) {
                    // Handle the case when the entered OTP is not a valid number
                }

                long currentTimeMillis = System.currentTimeMillis();

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    Map<String, Object> otpData = (Map<String, Object>) userSnapshot.getValue();
                    int otpValue = ((Number) otpData.get("otp")).intValue();
                    long expirationTimeMillis = ((Number) otpData.get("expiration")).longValue();

                    if (otpValue == enteredOtpInt && currentTimeMillis <= expirationTimeMillis) {
                        lock(otpData.get("boxID").toString());
                        otpReference.child(userId).removeValue();
                        otpValid=true;
                        break;
                    }
                }
                if(otpValid){

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle any errors
            }
        });
    }
}
