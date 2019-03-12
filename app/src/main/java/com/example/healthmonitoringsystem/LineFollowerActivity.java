package com.example.healthmonitoringsystem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TextView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import petrov.kristiyan.colorpicker.ColorPicker;


public class LineFollowerActivity extends BaseActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    String uName;
    int pos, col;
    String HexColor, LFcolors;
    ArrayList<String> colors = new ArrayList<>();
    ArrayList<String> colorsEmergency = new ArrayList<>();
    TextView lfcolor;


    int[] imageId = {
            R.drawable.bedroom,
            R.drawable.kitchen,
            R.drawable.livingroom,
            R.drawable.toilet,
            R.drawable.emergency,
    };
    String[] web = {"Bedroom", "Kitchen", "LivingRoom", "Toilet", "Emergency"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_line_follower, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(1).setChecked(true);
        colors.add("#ff0000");
        colors.add("#0000ff");
        colors.add("#ffff00");
        colors.add("#00ff00");
        colorsEmergency.add("#ff0000");
        colorsEmergency.add("#0000ff");
        colorsEmergency.add("#ffff00");
        colorsEmergency.add("#00ff00");
        SharedPreferences prefs = getSharedPreferences("Information", MODE_PRIVATE);
        uName = prefs.getString("Authcode", "");
        final DatabaseReference mRef = database.getReference("Patients").child(uName).child("LineFollowerInformation");
        ImageAdapter adapter = new ImageAdapter(this, web, imageId);

        GridView grid = (GridView) findViewById(R.id.gridView);
        grid.setNumColumns(2);
        grid.setVerticalSpacing(100);
        grid.setHorizontalSpacing(100);
        grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                if (position != 4)
                    openColorpicker();
                else
                    openColorpickerEmergency();
            }
        });
        lfcolor = findViewById(R.id.TVlfcolor);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    if(ds.getValue().equals("#ff0000")){
                        mRef.child(ds.getKey()).setValue("Red");
                    }
                    if(ds.getValue().equals("#0000ff")){
                        mRef.child(ds.getKey()).setValue("Blue");
                    }
                    if(ds.getValue().equals("#ffff00")){
                        mRef.child(ds.getKey()).setValue("Yellow");
                    }
                    if(ds.getValue().equals("#00ff00")){
                        mRef.child(ds.getKey()).setValue("Green");
                    }

                }
                LFcolors = "Bedroom: " + dataSnapshot.child("Bedroom").getValue() + "\n" + "Kitchen: " + dataSnapshot.child("Kitchen").getValue() + "\n"
                        + "LivingRoom: " + dataSnapshot.child("LivingRoom").getValue() + "\n" + "Toilet: " + dataSnapshot.child("Toilet").getValue() + "\n"
                        + "Emergency: " + dataSnapshot.child("Emergency").getValue();
                lfcolor.setText(LFcolors);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void openColorpicker() {
        ColorPicker colorPicker = new ColorPicker(LineFollowerActivity.this);

        colorPicker.setColors(colors).setColumns(2).setRoundColorButton(true).setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                DatabaseReference mRef = database.getReference("Patients").child(uName).child("LineFollowerInformation");
                col = color;
                if (pos == 0) {
                    HexColor = String.format("#%06x", (0xFFFFFF) & col);
                    mRef.child("Bedroom").setValue(HexColor);
                    colors.remove(String.format("#%06x", (0xFFFFFF) & col));
                }
                if (pos == 1) {
                    HexColor = String.format("#%06x", (0xFFFFFF) & col);
                    mRef.child("Kitchen").setValue(HexColor);
                    colors.remove(String.format("#%06x", (0xFFFFFF) & col));

                }
                if (pos == 2) {
                    HexColor = String.format("#%06x", (0xFFFFFF) & col);
                    mRef.child("LivingRoom").setValue(HexColor);
                    colors.remove(String.format("#%06x", (0xFFFFFF) & col));
                }

                if (pos == 3) {
                    HexColor = String.format("#%06x", (0xFFFFFF) & col);
                    mRef.child("Toilet").setValue(HexColor);
                    colors.remove(String.format("#%06x", (0xFFFFFF) & col));
                }

            }

            @Override
            public void onCancel() {

            }
        }).show();


    }

    public void openColorpickerEmergency() {
        ColorPicker colorPicker = new ColorPicker(LineFollowerActivity.this);

        colorPicker.setColors(colorsEmergency).setColumns(2).setRoundColorButton(true).setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position, int color) {
                DatabaseReference mRef = database.getReference("Patients").child(uName).child("LineFollowerInformation");
                col = color;
                HexColor = String.format("#%06x", (0xFFFFFF) & col);
                mRef.child("Emergency").setValue(HexColor);
            }

            @Override
            public void onCancel() {

            }
        }).show();


    }

}
