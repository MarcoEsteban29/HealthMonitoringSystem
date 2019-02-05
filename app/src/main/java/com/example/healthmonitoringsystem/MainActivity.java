package com.example.healthmonitoringsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText LogEmail, LogPassword,AuthCode;

   FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference mRef = database.getReference("Users");
    DatabaseReference mRef2 = database.getReference("Patients").child("Elderly1").child("Notification");
    String emailAdd,AuthCodes;
    String Password;
    String verify;
    String nAme, cp, email, age,femail, tokencompare;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.TVSignUp).setOnClickListener(this);
        findViewById(R.id.LogInButton).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        LogEmail = findViewById(R.id.LogInEmail);
        LogPassword = findViewById(R.id.LogInPassword);
        AuthCode = findViewById(R.id.LoginAuthCode);


    }
    private void userLogin() {
        emailAdd = LogEmail.getText().toString().trim();
        Password = LogPassword.getText().toString().trim();
        AuthCodes = AuthCode.getText().toString().trim();
        Log.d("wew",mRef2.child("Elderly1").getKey());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                verify = String.valueOf(dataSnapshot.child(emailAdd.replace(".", ",")).child("PersonalInformation").child("Verified").getValue());
                if (TextUtils.isEmpty(emailAdd)) {
                    LogEmail.requestFocus();
                    Toast.makeText(MainActivity.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Password)) {
                    LogPassword.requestFocus();
                    Toast.makeText(MainActivity.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(AuthCodes)) {
                    LogPassword.requestFocus();
                    Toast.makeText(MainActivity.this, "Please Enter AuthenticationCode", Toast.LENGTH_SHORT).show();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches()) {
                    Toast.makeText(MainActivity.this, "Please Enter a Valid Email", Toast.LENGTH_SHORT).show();
                    LogEmail.requestFocus();

                } else if (LogPassword.length() < 6) {
                    Toast.makeText(MainActivity.this, "The Password should be 6 characters and above", Toast.LENGTH_SHORT).show();
                    LogPassword.requestFocus();
                }
                else if (verify.equals("false")) {
                    Toast.makeText(MainActivity.this, "Please Verify your Email first.", Toast.LENGTH_SHORT).show();
                    (Objects.requireNonNull(mAuth.getCurrentUser())).reload().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mRef.child(emailAdd.replace(".", ",")).child("PersonalInformation").child("Verified").setValue(String.valueOf(mAuth.getCurrentUser().isEmailVerified()));
                        }
                    });

                }else if (!AuthCodes.equals(mRef2.child("Elderly1").getKey())) {
                    LogPassword.requestFocus();
                    Toast.makeText(MainActivity.this, "No Valid Authentication Code", Toast.LENGTH_SHORT).show();
                }
                else {
//
                    mAuth.signInWithEmailAndPassword(emailAdd, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                mRef.child(emailAdd.replace(".", ",")).child("PersonalInformation").child("AuthCode").setValue(AuthCodes);
//
                                mRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        nAme = String.valueOf(dataSnapshot.child(emailAdd.replace(".", ",")).child("PersonalInformation").child("Name").getValue());
                                        cp = String.valueOf(dataSnapshot.child(emailAdd.replace(".", ",")).child("PersonalInformation").child("Cp").getValue());
                                        age = String.valueOf(dataSnapshot.child(emailAdd.replace(".", ",")).child("PersonalInformation").child("Age").getValue());
                                        email = String.valueOf(dataSnapshot.child(emailAdd.replace(".", ",")).child("PersonalInformation").child("Email").getValue());
                                        femail = email.replace(".",",");
                                        SharedPreferences.Editor editor = getSharedPreferences("Information", MODE_PRIVATE).edit();
                                        editor.putString("name", nAme);
                                        editor.putString("Authcode",AuthCodes);
                                        editor.putString("cp", cp);
                                        editor.putString("email", femail);
                                        editor.putString("age", age);
                                        editor.apply();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {


                                    }
                                });

                                mRef2.child(FirebaseInstanceId.getInstance().getToken()).setValue("true");
//                                mRef2.addValueEventListener(new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        if (dataSnapshot.hasChildren())
//                                        {
//                                            for(DataSnapshot ds : dataSnapshot.getChildren()){
//                                                if (ds.getValue().equals(tokencompare))
//                                                {
//                                                    break;
//                                                }
//                                                else{
//
//                                                }
//                                            }
//
//
//                                        }
//                                        else{
//                                            mRef2.push().setValue(FirebaseInstanceId.getInstance().getToken());
//                                        }
//                                    }
//
//                                    @Override
//                                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                    }
//                                });


                                Intent intent = new Intent(MainActivity.this, MonitorActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                finish();
                                startActivity(intent);



                            } else {
                                Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.TVSignUp:
                startActivity(new Intent(this, UserSignUp.class));
                break;
            case R.id.LogInButton:
                userLogin();
                break;
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
