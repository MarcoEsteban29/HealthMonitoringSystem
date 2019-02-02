package com.example.healthmonitoringsystem;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Temperature extends BaseActivity implements AdapterView.OnItemSelectedListener{
    TextView TempData,Dates;
    String tempString = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference("Temperature");
    long dates;
    String[] options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_temperature, contentFrameLayout);
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(3).setChecked(true);

        Spinner spinner = (Spinner) findViewById(R.id.searchSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Month,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        options = Temperature.this.getResources().getStringArray(R.array.Month);


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(options[position].equals("January"))
        {

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
