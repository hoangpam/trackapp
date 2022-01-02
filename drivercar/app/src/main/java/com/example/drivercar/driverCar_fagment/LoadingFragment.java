package com.example.drivercar.driverCar_fagment;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercar.R;
import com.example.drivercar.adapter_Driver.AdapterCars;
import com.example.drivercar.adapter_customer.AdapterInfomation;
import com.example.drivercar.model.ModelCars;
import com.example.drivercar.model.ModelInfomation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class LoadingFragment extends Fragment {

    private ArrayList<ModelInfomation> infomationArrayList;
    private AdapterInfomation adapterInfomation;
    FirebaseAuth firebaseAuth;
    RecyclerView Recycle_menu2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_loading, null);
        setHasOptionsMenu(true);
        firebaseAuth = FirebaseAuth.getInstance();
        Recycle_menu2 = v.findViewById(R.id.Recycle_menu2);
        loadAllCars();
        Recycle_menu2.setHasFixedSize(true);
        Recycle_menu2.setLayoutManager(new LinearLayoutManager(getContext()));
        Recycle_menu2.setItemAnimator(new DefaultItemAnimator());
        adapterInfomation = new AdapterInfomation(getContext(),infomationArrayList);
        Recycle_menu2.setAdapter(adapterInfomation);
        return v;
    }
    private void loadAllCars() {
        infomationArrayList = new ArrayList<>();
        String timestamp = ""+System.currentTimeMillis();
        //get all product
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users");


         reference.child("Infomations")
           .addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot snapshot) {
                  if(snapshot.exists()){
                   //before getting reset list
                  infomationArrayList.clear();
                  for (DataSnapshot ds:snapshot.getChildren()){
                       ModelInfomation modelInfomation = ds.getValue(ModelInfomation.class);
                        infomationArrayList.add(modelInfomation);
                  }

                  //setup adapter
                  adapterInfomation =new AdapterInfomation(getContext(),infomationArrayList);
                   //set adapter
                  Recycle_menu2.setAdapter(adapterInfomation);
                  }
              }
              @Override
              public void onCancelled(@NonNull DatabaseError error) {
              }
           });

    }

    }
