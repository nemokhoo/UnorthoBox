package com.example.unorthobox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class DeliverActivity extends AppCompatActivity {
    private EditText otpField1, otpField2, otpField3, otpField4;
    private ImageView deliverySubmit;
    private FirebaseAuth mAuth;
    boolean lock_status = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.delivery_page);
        otpField1=findViewById(R.id.otpField1);
        otpField2= findViewById(R.id.otpField2);
        otpField3 = findViewById(R.id.otpField3);
        otpField4 = findViewById(R.id.otpField4);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        setupOTPInputs();


        deliverySubmit = findViewById(R.id.deliverySubmit);
        deliverySubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unlock();

            }
        });
    }

    private void unlock() {
        String topic= otpField1.getText().toString()+otpField2.getText().toString()+otpField3.getText().toString()+otpField4.getText().toString();
        Context context = this;
        CharSequence text = "Locked!";
        int duration = Toast.LENGTH_LONG;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout: {
                mAuth.signOut(); // Sign out the user

                // Redirect the user to the login activity
                Intent intent = new Intent(DeliverActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Remove the current activity from the back stack
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
    private void setupOTPInputs() {
        setupOTPInputFocusChangeListener(otpField1, otpField2);
        setupOTPInputFocusChangeListener(otpField2, otpField3);
        setupOTPInputFocusChangeListener(otpField3, otpField4);
    }

    private void setupOTPInputFocusChangeListener(EditText currentField, EditText nextField) {
        currentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    nextField.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}