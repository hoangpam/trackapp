package com.example.drivercar.driverCar_fagment;

import android.content.Intent;
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
import com.example.drivercar.activity.ActivityAddInfoDriver;
import com.example.drivercar.adapter_Driver.AdapterCars;
import com.example.drivercar.model.ModelCars;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MySeftFragment extends Fragment {

    private RecyclerView Recycle_menu;
    private ArrayList<ModelCars> carsList;
    private AdapterCars adapterCars;
    FirebaseAuth firebaseAuth;
    private Button AddDriverBtn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_seft,null);
        // Inflate the layout for this fragment
        firebaseAuth = FirebaseAuth.getInstance();
        Recycle_menu = (RecyclerView) v.findViewById(R.id.Recycle_menu);
        AddDriverBtn = (Button) v.findViewById(R.id.AddDriverBtn);
        AddDriverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent a = new Intent(getContext(), ActivityAddInfoDriver.class);
                startActivity(a);
            }
        });
        loadAllCars();
        Recycle_menu.setHasFixedSize(true);
        Recycle_menu.setLayoutManager(new LinearLayoutManager(getContext()));
        Recycle_menu.setItemAnimator(new DefaultItemAnimator());
        adapterCars = new AdapterCars(getContext(),carsList);
        Recycle_menu.setAdapter(adapterCars);
        return v;
    }

    private void loadAllCars() {
        carsList = new ArrayList<>();

        //get all product
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users");
        reference.child("Cars")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //before getting reset list
                        carsList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelCars modelCars = ds.getValue(ModelCars.class);
                            carsList.add(modelCars);
                        }
                        //setup adapter
                        adapterCars =new AdapterCars(getContext(),carsList);
                        //set adapter
                        Recycle_menu.setAdapter(adapterCars);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}