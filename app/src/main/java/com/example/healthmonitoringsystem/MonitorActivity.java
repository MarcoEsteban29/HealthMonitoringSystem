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

    Button record;
   ImageView temp;
    TextView heartRate;
    int dataCount = 0;
    int count = 0;
    String uName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_monitor, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference hrRef1 = database.getReference("HeartRateTemporary");
        hrRef1.removeValue();

        SharedPreferences pref = getSharedPreferences("Information",MODE_PRIVATE);
        uName = pref.getString("email","");
        temp = findViewById(R.id.temp);
        heartRate = findViewById(R.id.heartrate);
        record= findViewById(R.id.btrecord);



        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("HeartRateTemporary");

    }

}
