package com.example.healthmonitoringsystem;

import android.net.IpSecManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Temperature extends BaseActivity implements AdapterView.OnItemSelectedListener{
    TextView HRData,Dates;
    String HRString = "";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference("Patients").child("Elderly1").child("HRRecord");
    String[] dates;
    String month;

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
        HRData = findViewById(R.id.RecordTVs);
        options = Temperature.this.getResources().getStringArray(R.array.Month);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                     dates = ds.getKey().split(",");
                     Log.d("munth",dates[0]);
                     if(dates[0].equals("1"));
                    {
                        month = "January" ;

                        for(DataSnapshot data : ds.getChildren())
                        {
                            HRString += dates[1] + "   "+ data.getValue() + "\n";
                        }

                    }
                    if(dates[0].equals("2"));
                    {
                        month = "February" ;
                        for(DataSnapshot data : ds.getChildren())
                        {
                            HRString += dates[1] + "   "+ data.getValue() + "\n";
                        }
                    }
                    if(dates[0].equals("3"));
                    {
                        month = "March" ;
                        for(DataSnapshot data : ds.getChildren())
                        {
                            HRString +=dates[1] + "   "+ data.getValue() + "\n";
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("month", options[position]);
        if(options[position].equals(month) )
        {
            HRData.setText(" ");
            HRData.setText(HRString);
        }
        if(options[position].equals(month)  )
        {
            HRData.setText(" ");
            HRData.setText(HRString);
        }
        if(options[position].equals(month)  )
        {
            HRData.setText(" ");
            HRData.setText(HRString);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
