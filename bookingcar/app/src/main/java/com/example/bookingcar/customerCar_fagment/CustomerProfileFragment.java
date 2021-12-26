package com.example.bookingcar.customerCar_fagment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.bookingcar.R;
import com.example.bookingcar.activity.MainMenu;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import javax.annotation.Nullable;

public class CustomerProfileFragment extends Fragment implements View.OnClickListener {


    //khai bao biến

    TextView nameTv;
    Button logoutBtn;
    LinearLayout ll1,ll2,ll3,ll4;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customerprofile, null);
        nameTv = (TextView) v.findViewById(R.id.nameTv);

        logoutBtn = (Button) v.findViewById(R.id.logoutBtn);

        ll1 = (LinearLayout) v.findViewById(R.id.ll1);

        ll2 = (LinearLayout) v.findViewById(R.id.ll2);

        ll3 = (LinearLayout) v.findViewById(R.id.ll3);

        ll4 = (LinearLayout) v.findViewById(R.id.ll4);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //make offline
                //sign out
                //go to login activity
                makeMeOffline();
            }
        });
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        ll4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return v;
    }
    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null)
        {
            Intent intent = new Intent(new Intent(getActivity().getApplication(), MainMenu.class));
            startActivity(intent);
        }
        else {
            loadMyInfo();
        }
    }

    private void loadMyInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("UID").equalTo(firebaseAuth.getUid())
                .addValueEventListener( new ValueEventListener(){

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String fname =""+ds.child("FirstName").getValue();
                            String name = ""+ds.child("LastName").getValue();


                            nameTv.setText(""+fname+" "+name);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    private void makeMeOffline()
    {
        //after logging in make user online
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang đang xuất khỏi hệ thống...");
        progressDialog.show();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Online","false");

        //update value to db
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        firebaseAuth.signOut();
                        progressDialog.dismiss();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {


        }
    }
}
