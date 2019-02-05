package com.example.healthmonitoringsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.FrameLayout;

import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;




public class MonitorActivity extends BaseActivity {


    TextView heartRate,temp;

    String uName,auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_monitor, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);




        SharedPreferences pref = getSharedPreferences("Information",MODE_PRIVATE);
        uName = pref.getString("email","");
        auth = pref.getString("Authcode","");
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference hrRef = database.getReference("Patients").child(auth).child("HeartRateValue");
        DatabaseReference TRef = database.getReference("Patients").child(auth).child("TemperatureValue");
        heartRate = findViewById(R.id.HRvalue);
        temp = findViewById(R.id.TempValue);


        hrRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                heartRate.setText(String.valueOf(dataSnapshot.getValue())+ " BPM");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        TRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                temp.setText(String.valueOf(dataSnapshot.getValue()) + " C");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





    }

}
