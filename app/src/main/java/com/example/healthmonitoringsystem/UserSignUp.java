package com.example.healthmonitoringsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.FirebaseDatabase;

public class UserSignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText email, password, name, cpNum, age;

    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    String Verified,Bedroom = " ",Kitchen = " ",LivingRoom = " ",Toilet = " ",Emergency = " ",Uri ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);


        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        email = findViewById(R.id.SignUpEmail);
        password = findViewById(R.id.SignUpPassword);
        name = findViewById(R.id.SignUpName);
        cpNum = findViewById(R.id.SignUpNum);
        age = findViewById(R.id.SignUpAge);
        findViewById(R.id.TVLogIn).setOnClickListener(this);
        findViewById(R.id.SignUpButton).setOnClickListener(this);
    }
    private void registerUser() {
        final String emailAdd = email.getText().toString().trim();
        final String Password = password.getText().toString().trim();
        final String Name = name.getText().toString().trim();
        final String CpNum = cpNum.getText().toString().trim();
        final String Age = age.getText().toString().trim();


        if (TextUtils.isEmpty(emailAdd) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(Name) || TextUtils.isEmpty(Age) || TextUtils.isEmpty(CpNum)) {
            name.requestFocus();
            Toast.makeText(UserSignUp.this, "Please complete the information required", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAdd).matches()) {
            Toast.makeText(UserSignUp.this, "Please Enter a Valid Email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
        } else if (password.length() < 6) {
            Toast.makeText(UserSignUp.this, "The Password should be 6 characters and above", Toast.LENGTH_SHORT).show();
            password.requestFocus();
        } else if (cpNum.length() != 11) {
            Toast.makeText(UserSignUp.this, "Please enter a valid Cellphone number", Toast.LENGTH_SHORT).show();
            cpNum.requestFocus();
        } else {
            progressDialog.setMessage("Registering User...");
            progressDialog.show();

            firebaseAuth.createUserWithEmailAndPassword(emailAdd, Password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        firebaseAuth.getCurrentUser().reload();
                        Verified = String.valueOf(firebaseAuth.getCurrentUser().isEmailVerified());
                        User user = new User(Name, Age, CpNum, emailAdd, Password,Uri,Verified);

                        FirebaseDatabase.getInstance().getReference("Users").child(emailAdd.replace(".", ",")).child("PersonalInformation")
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    LineFollowerInfo lfInfo = new LineFollowerInfo(Bedroom,Kitchen,LivingRoom,Toilet,Emergency);
                                    FirebaseDatabase.getInstance().getReference("Users").child(emailAdd.replace(".", ",")).child("LineFollowerInformation").setValue(lfInfo);
                                    Toast.makeText(UserSignUp.this, "Registered Successfully,   Email Verification sent to " + firebaseAuth.getCurrentUser().getEmail(), Toast.LENGTH_SHORT).show();
                                    firebaseAuth.getCurrentUser().sendEmailVerification();
                                    firebaseAuth.getCurrentUser().reload();
                                    progressDialog.dismiss();
                                    email.setText(null);
                                    password.setText(null);
                                    name.setText(null);
                                    cpNum.setText(null);
                                    age.setText(null);

                                }
                            }
                        });


                    } else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "The email is already used", Toast.LENGTH_SHORT).show();
                            email.requestFocus();
                            progressDialog.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();


                        }
                    }

                }
            });
        }


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.SignUpButton:
                registerUser();
                break;

            case R.id.TVLogIn:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }


    }

    @Override
    public void onBackPressed() {
        firebaseAuth.getCurrentUser().reload();
        super.onBackPressed();
    }
}