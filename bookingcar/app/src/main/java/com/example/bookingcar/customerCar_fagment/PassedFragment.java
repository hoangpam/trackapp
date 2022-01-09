package com.example.bookingcar.customerCar_fagment;

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

import com.example.bookingcar.R;
import com.example.bookingcar.adapter_Customer.AdapterOrderDeliveryGo;
import com.example.bookingcar.model.OrderDeliveryGo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PassedFragment extends Fragment {
    private ArrayList<OrderDeliveryGo> orderDeliveryGoArrayList;
    private AdapterOrderDeliveryGo adapterOrderDeliveryGo;
    FirebaseAuth firebaseAuth;
    RecyclerView Recycle_menu2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_passed,null);
        setHasOptionsMenu(true);
        firebaseAuth = FirebaseAuth.getInstance();
        Recycle_menu2 = v.findViewById(R.id.Recycle_menu2);
        loadAllLoadingOrder();
        Recycle_menu2.setHasFixedSize(true);
        Recycle_menu2.setLayoutManager(new LinearLayoutManager(getContext()));
        Recycle_menu2.setItemAnimator(new DefaultItemAnimator());
        return v;
    }
    private void loadAllLoadingOrder() {
        orderDeliveryGoArrayList = new ArrayList<>();
        String timestamp = ""+System.currentTimeMillis();
        //get all order product
        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("Users");

        reference.child("OrderDeliveryGo")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            //before getting reset list
                            orderDeliveryGoArrayList.clear();
                            for (DataSnapshot ds:snapshot.getChildren()){
                                OrderDeliveryGo orderDeliveryGo = ds.getValue(OrderDeliveryGo.class);
                                orderDeliveryGoArrayList.add(orderDeliveryGo);
                            }

                            //setup adapter
                            adapterOrderDeliveryGo =new AdapterOrderDeliveryGo(getContext(),orderDeliveryGoArrayList);
                            //set adapter
                            Recycle_menu2.setAdapter(adapterOrderDeliveryGo);
                        }else{
                            adapterOrderDeliveryGo.notifyDataSetChanged();
                            adapterOrderDeliveryGo.notifyItemChanged(0);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });

    }
}
