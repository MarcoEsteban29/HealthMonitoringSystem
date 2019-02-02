package com.example.healthmonitoringsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;


import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends BaseActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference("Users");
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
     String  uName,Picture;
    final static int Gallery_Pick = 1;
    Uri uri;
    TextView tvName, tvCp, tvEmail, tvAge;
    CircleImageView ProfileImage;
    private Intent CropIntent;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout contentFrameLayout = (FrameLayout) findViewById(R.id.content_frame); //Remember this is the FrameLayout area within your activity_main.xml
        getLayoutInflater().inflate(R.layout.activity_profile, contentFrameLayout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(2).setChecked(true);
        SharedPreferences pref = getSharedPreferences("Information",MODE_PRIVATE);
        uName = pref.getString("email","");
        Log.d("email",uName);
        tvName = (TextView) findViewById(R.id.TvUserName);
        tvCp = (TextView) findViewById(R.id.TvCp);
        tvEmail = (TextView) findViewById(R.id.TvEmail);
        tvAge = (TextView) findViewById(R.id.TvAge);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvName.setText(String.valueOf(dataSnapshot.child(uName).child("PersonalInformation").child("Name").getValue()));
                tvCp.setText(String.valueOf(dataSnapshot.child(uName).child("PersonalInformation").child("Cp").getValue()));
                tvEmail.setText(String.valueOf(dataSnapshot.child(uName).child("PersonalInformation").child("Email").getValue()));
                tvAge.setText(String.valueOf(dataSnapshot.child(uName).child("PersonalInformation").child("Age").getValue()));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ProfileImage = findViewById(R.id.profile);
        Uri uri  = Uri.parse("android.resource://"+this.getPackageName()+"/"+R.drawable.toilet);
        Picture = pref.getString("URI",uri.toString());
        Picasso.with(this).load(Picture).into(ProfileImage);


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setAspectRatio(1, 1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfileActivity.this);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultUri = result.getUri();
                 mRef.child(uName).child("PersonalInformation").child("Picture").setValue(resultUri.toString());
                SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Information",MODE_PRIVATE).edit();
                editor.putString("URI", resultUri.toString());
                editor.apply();



            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, "" + error, Toast.LENGTH_SHORT).show();
            }


        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent (ProfileActivity.this,MonitorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
        finish();
    }
}
