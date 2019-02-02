package com.example.healthmonitoringsystem;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Patients").child("Elderly1").child("Notification");
        reference.child("token").setValue(refreshToken);
    }
}
