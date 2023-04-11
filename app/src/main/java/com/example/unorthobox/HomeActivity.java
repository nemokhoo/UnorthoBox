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
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
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
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            checkUserRole();
        }
    }
    @Override
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
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Remove the current activity from the back stack
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    public void checkUserRole() {
        String userId = mAuth.getCurrentUser().getUid();
        if (userId != null) {
            DatabaseReference userRoleRef = FirebaseDatabase.getInstance().getReference("users/" + userId + "/role");
            userRoleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String userRole = dataSnapshot.getValue().toString();
                        Intent intent;
                        if ("user".equals(userRole)) {
                            binding = HomePageBinding.inflate(getLayoutInflater());
                            setContentView(binding.getRoot());
                            replaceFragment(new HomeFragments());
                            setSupportActionBar(binding.toolbar);

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
                        } else if ("delivery".equals(userRole)) {
                            intent = new Intent(HomeActivity.this, DeliverActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Handle error (unknown role)
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }



    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout2,fragment);
        fragmentTransaction.commit();
    }







}