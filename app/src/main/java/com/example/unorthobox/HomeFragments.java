package com.example.unorthobox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragments#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragments extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button lockButton, OTPButton;

    TextView boxIDView;

    boolean lock_status = true;
    private static final String USERS = "Users";

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = rootRef.child(USERS);
    FirebaseAuth mAuth;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String currentOtpKey;
    private int currentOtp;
    private DatabaseReference otpReference;
    private TextView textView;
    public HomeFragments() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragments newInstance(String param1, String param2) {
        HomeFragments fragment = new HomeFragments();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_fragments, container, false);
      lockButton =  view.findViewById(R.id.lockBtn);
        OTPButton = view.findViewById(R.id.otpBtn);
        textView= view.findViewById(R.id.otpText);

        otpReference = FirebaseDatabase.getInstance().getReference("otps");
        lockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserEmail();
            }
        });
        OTPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndStoreOTP();
            }
        });
        return view;
    }

    private void lock(String topic){

        Context context = getContext();
        CharSequence text = "Locked!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        MqttConnection mqttInstance = MqttConnection.getInstance(context);
        if(lock_status){
            mqttInstance.publish(topic, "unlock", context);
        }
        else{
            mqttInstance.publish(topic, "lock", context);
        }

        lock_status = !lock_status;
    }
    private void getUserEmail() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userEmailRef = database.getReference("users/" + getCurrentUserId() + "/boxID");
        userEmailRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lock(dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private String getCurrentUserId() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return currentUser.getUid();
        }
        return null;
    }
    public void generateAndStoreOTP() {
        String userId = getCurrentUserId();

        getUserBoxID(new BoxIDCallback() {
            @Override
            public void onCallback(String boxID) {
                int otp = new Random().nextInt(900000) + 100000; // Generate a 6-digit OTP
                long currentTimeMillis = System.currentTimeMillis();
                long expirationTimeMillis = currentTimeMillis + (5 * 60 * 1000); // 5 minutes from now
                textView.setText(Integer.toString(otp));
                Map<String, Object> otpData = new HashMap<>();
                otpData.put("otp", otp);
                otpData.put("expiration", expirationTimeMillis);
                otpData.put("boxID", boxID);
                otpReference.child(userId).setValue(otpData);
                Toast.makeText(getContext(), "OTP generated and stored.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getUserBoxID(BoxIDCallback boxIDCallback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userEmailRef = database.getReference("users/" + getCurrentUserId() + "/boxID");
        userEmailRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String boxID = dataSnapshot.getValue().toString();
                boxIDCallback.onCallback(boxID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    interface BoxIDCallback {
        void onCallback(String boxID);
    }

}