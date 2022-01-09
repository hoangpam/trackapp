package com.example.drivercar.driverCar_fagment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercar.R;
import com.example.drivercar.adapter_Driver.AdapterSpendingDriver;
import com.example.drivercar.adapter_customer.AdapterInfomation;
import com.example.drivercar.model.ModelInfomation;
import com.example.drivercar.model.SpendingDriver;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QuotedFragment extends Fragment {

    private ArrayList<SpendingDriver> spendingDriverArrayList;
    private AdapterSpendingDriver adapterSpendingDriver;
    FirebaseAuth firebaseAuth;
    private RecyclerView Recycle_menu2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View v = inflater.inflate(R.layout.fragment_quoted,null);


        Recycle_menu2 = v.findViewById(R.id.Recycle_menu2);
        firebaseAuth = FirebaseAuth.getInstance();

        spendingDriverArrayList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users");
        reference.child("SpendingDriver")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            spendingDriverArrayList.clear();
                            for (DataSnapshot ds:snapshot.getChildren())
                            {
                                SpendingDriver spendingDriver = ds.getValue(SpendingDriver.class);
                                spendingDriverArrayList.add(spendingDriver);
                            }
                            //setup adapter
                            adapterSpendingDriver =new AdapterSpendingDriver(getContext(),spendingDriverArrayList);
                            //set adapter
                            Recycle_menu2.setAdapter(adapterSpendingDriver);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        Recycle_menu2.setHasFixedSize(true);
        Recycle_menu2.setLayoutManager(new LinearLayoutManager(getContext()));
        Recycle_menu2.setItemAnimator(new DefaultItemAnimator());
        adapterSpendingDriver = new AdapterSpendingDriver(getContext(),spendingDriverArrayList);
        Recycle_menu2.setAdapter(adapterSpendingDriver);



        return v;
    }


}
