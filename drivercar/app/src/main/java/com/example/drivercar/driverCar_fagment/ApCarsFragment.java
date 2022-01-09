package com.example.drivercar.driverCar_fagment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.drivercar.R;
import com.example.drivercar.adapter_Driver.AdapterOrderDelivery;
import com.example.drivercar.model.OrderDelivery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApCarsFragment extends Fragment {

    private ArrayList<OrderDelivery> orderDeliveryArrayList;
    private AdapterOrderDelivery adapterOrderDelivery;
    FirebaseAuth firebaseAuth;
    RecyclerView Recycle_menu2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_ap_cars,null);
        setHasOptionsMenu(true);
        firebaseAuth = FirebaseAuth.getInstance();
        Recycle_menu2 = v.findViewById(R.id.Recycle_menu2);
        loadAllLoadingOrder();
        Recycle_menu2.setHasFixedSize(true);
        Recycle_menu2.setLayoutManager(new LinearLayoutManager(getContext()));
        Recycle_menu2.setItemAnimator(new DefaultItemAnimator());
        adapterOrderDelivery = new AdapterOrderDelivery(getContext(),orderDeliveryArrayList);
        Recycle_menu2.setAdapter(adapterOrderDelivery);

        return v;
    }
    private void loadAllLoadingOrder() {
        orderDeliveryArrayList = new ArrayList<>();
        String timestamp = ""+System.currentTimeMillis();
        //get all order product
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users");

        reference.child("OrderDelivery")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            //before getting reset list
                            orderDeliveryArrayList.clear();
                            for (DataSnapshot ds:snapshot.getChildren()){
                                OrderDelivery orderDelivery = ds.getValue(OrderDelivery.class);
                                orderDeliveryArrayList.add(orderDelivery);
                            }

                            //setup adapter
                            adapterOrderDelivery =new AdapterOrderDelivery(getContext(),orderDeliveryArrayList);
                            //set adapter
                            Recycle_menu2.setAdapter(adapterOrderDelivery);
                        }else{
                            adapterOrderDelivery.notifyDataSetChanged();
                            adapterOrderDelivery.notifyItemChanged(0);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }
}
