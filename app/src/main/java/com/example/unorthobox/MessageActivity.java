package com.example.unorthobox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MessageActivity extends AppCompatActivity {

    EditText responseText;
    ImageView backButton, sendButton, profileButton;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_page);

        backButton = findViewById(R.id.messageBack);
        sendButton = findViewById(R.id.sendButton);
        profileButton = findViewById(R.id.profilePicture);

        responseText = findViewById(R.id.response);
        mAuth= FirebaseAuth.getInstance();

        backButton.setOnClickListener(view -> finish());
        profileButton.setOnClickListener(view -> toProfile());
        sendButton.setOnClickListener(view -> sendMessage());
    }

    private void toProfile(){
        Context context = getApplicationContext();
        CharSequence text = "You are trying to visit your friend's profile, but this feature has not been implemented!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void sendMessage(){
        Context context = getApplicationContext();
        CharSequence text = "Your message is sent!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
            {
                mAuth.signOut();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

}