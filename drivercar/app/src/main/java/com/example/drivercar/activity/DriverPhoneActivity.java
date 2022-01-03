package com.example.drivercar.activity;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.drivercar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class DriverPhoneActivity extends AppCompatActivity {
    private ImageButton callBtn;
    private String phone;
    DatabaseReference ref  = FirebaseDatabase.getInstance().getReference("Users");
    private FirebaseAuth firebaseAuth;
    Button receorder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_phone);
        firebaseAuth = FirebaseAuth.getInstance();
        loadCustomer();
        callBtn = findViewById(R.id.callBtn);
        receorder = findViewById(R.id.receorder);

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhone();
            }
        });

        receorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void loadCustomer(){
        ref.orderByChild("AccountType").equalTo("Customer")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        phone = ""+snapshot.child("MobileNo").getValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    String phone1 = "0389346227";

    private void dialPhone() {

        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(phone1))));
        Toasty.info(this, ""+phone1, Toast.LENGTH_SHORT, true).show();

    }
}