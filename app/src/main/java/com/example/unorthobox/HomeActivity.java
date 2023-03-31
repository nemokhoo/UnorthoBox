package com.example.unorthobox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.unorthobox.databinding.HomePageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeActivity extends AppCompatActivity {

    ImageView lockButton, OTPButton, backButton, newMessageButton, userButton, homeButton;

    TextView boxIDView;

    boolean lock_status = true;
    private static final String USERS = "Users";

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = rootRef.child(USERS);
    FirebaseAuth mAuth;
    HomePageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeFragments homeFragment = new HomeFragments();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayout2, homeFragment)
                .commit();
//        setContentView(R.layout.home_page);
//
//        boxIDView = findViewById(R.id.boxIDshow);
//
//        Intent intent = getIntent();
//        String email = intent.getStringExtra("email");
//
//
//        lockButton = findViewById(R.id.lockButton);
//        OTPButton = findViewById(R.id.otpButton);
//        backButton = findViewById(R.id.homeBack);
//
//        newMessageButton = findViewById(R.id.homeNewMessage);
//        userButton = findViewById(R.id.homeUser);
//        homeButton = findViewById(R.id.homeHome);
//
//        lockButton.setOnClickListener(view -> lock());
//        backButton.setOnClickListener(view -> finish());
//        OTPButton.setOnClickListener(view -> toOTP());
//
//        newMessageButton.setOnClickListener(view -> toNotifications());
//        userButton.setOnClickListener(view -> toUser());
//        homeButton.setOnClickListener(view -> toHome());
//        mAuth= FirebaseAuth.getInstance();
//
//
//
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                System.out.println("------------going through?---------" + email);
//                for(DataSnapshot ds: snapshot.getChildren()) {
//                    if (ds.child("email").getValue().toString().equals(email)) {
//                        System.out.println("-----------YES IT WORKED?!----------");
//                        boxIDView.setText(ds.child("boxID").getValue(String.class));
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        binding = HomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView2.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.homefrag:
                    replaceFragment(new HomeFragments());
                    break;
                case R.id.profile:
                    replaceFragment(new NotificationFragment());
                    break;
                case R.id.Notification:
                        replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout2,fragment);
        fragmentTransaction.commit();
    }

  // @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.home_page);
//
//        boxIDView = findViewById(R.id.boxIDshow);
//
//        Intent intent = getIntent();
//        String email = intent.getStringExtra("email");
//
//
//        lockButton = findViewById(R.id.lockButton);
//        OTPButton = findViewById(R.id.otpButton);
//        backButton = findViewById(R.id.homeBack);
//
//        newMessageButton = findViewById(R.id.homeNewMessage);
//        userButton = findViewById(R.id.homeUser);
//        homeButton = findViewById(R.id.homeHome);
//
//        lockButton.setOnClickListener(view -> lock());
//        backButton.setOnClickListener(view -> finish());
//        OTPButton.setOnClickListener(view -> toOTP());
//
//        newMessageButton.setOnClickListener(view -> toNotifications());
//        userButton.setOnClickListener(view -> toUser());
//        homeButton.setOnClickListener(view -> toHome());
//        mAuth= FirebaseAuth.getInstance();
//
//
//
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                System.out.println("------------going through?---------" + email);
//                for(DataSnapshot ds: snapshot.getChildren()) {
//                    if (ds.child("email").getValue().toString().equals(email)) {
//                        System.out.println("-----------YES IT WORKED?!----------");
//                        boxIDView.setText(ds.child("boxID").getValue(String.class));
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//
//    }
//    public void onStart(){
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user==null){
//            startActivity(new Intent(HomeActivity.this, StartActivity.class));
//        }
//    }

    private void lock(){
        Context context = getApplicationContext();
//        CharSequence text = "Locked!";
//        int duration = Toast.LENGTH_SHORT;
//
//        Toast toast = Toast.makeText(context, text, duration);
//        toast.show();

        MqttConnection mqttInstance = MqttConnection.getInstance(context);
        if(lock_status){
            mqttInstance.publish("Unorthobox", "unlock", context);
        }
        else{
            mqttInstance.publish("Unorthobox", "lock", context);
        }

        lock_status = !lock_status;
    }

    private void toOTP(){
        Intent switchActivityIntent = new Intent(this, OTPActivity.class);
        startActivity(switchActivityIntent);
    }

    private void toNotifications(){
        Intent switchActivityIntent = new Intent(this, NotificationActivity.class);
        startActivity(switchActivityIntent);
    }
    private void toHome(){
        Context context = getApplicationContext();
        CharSequence text = "You are already home!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    private void toUser(){
        Intent switchActivityIntent = new Intent(this, ProfileActivity.class);
        startActivity(switchActivityIntent);
    }

}