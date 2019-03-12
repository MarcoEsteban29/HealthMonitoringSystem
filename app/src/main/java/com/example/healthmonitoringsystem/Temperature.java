package com.example.healthmonitoringsystem;

import android.net.IpSecManager;
import android.os.Bundle;
import android.provider.ContactsContract;
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
    TextView HRData;
    String HRString = "";
    String HRTitle = "Heart Rate Record"+"\n"+"------------------------------------------------------------------------"+"\n"+"Day  "+"\t"+"Time/Value";
    String TempString = "";
    String TempTitle = "Temperature Record"+"\n"+"------------------------------------------------------------------------"+"\n"+"Day  "+"\t"+"Time/Value";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference("Patients").child("Elderly1").child("HRRecord");
    DatabaseReference mRef1 = database.getReference("Patients").child("Elderly1").child("TempRecord");
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

                    if(dates[0].equals("2"));
                    {

                        for (DataSnapshot ds1 : ds.getChildren()) {
                            String[] values1 = ds1.getValue().toString().split("/");
                            int integers1 = Double.valueOf(values1[1]).intValue();
                            HRString = HRString + (dates[1] + "     " +values1[0]+" / "+integers1 +" BPM"+ "\n") + "------------------------------------------------------------------------" + "\n";

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    dates = ds.getKey().split(",");

                    if(dates[0].equals("2"));
                    {
                        for(DataSnapshot ds1 : ds.getChildren()){
                            String [] values = ds1.getValue().toString().split("/");

                            int integers = Double.valueOf(values[1]).intValue();


                            TempString +=(dates[1] + "     " +values[0]+" / "+ integers + "°C"+"\n")+"------------------------------------------------------------------------"+"\n";

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


        if(options[position].equals("February"))
        {
                HRData.setText(" ");
                HRData.setText(HRTitle+"\n"+"------------------------------------------------------------------------"+"\n"+HRString+"\n"+TempTitle+"\n"+"------------------------------------------------------------------------"+"\n"+TempString);


        }
        if(!options[position].equals("February")){
            HRData.setText(" ");
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
