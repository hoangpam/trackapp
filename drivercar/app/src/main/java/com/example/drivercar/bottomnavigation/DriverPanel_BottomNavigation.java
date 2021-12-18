package com.example.drivercar.bottomnavigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.drivercar.R;
import com.example.drivercar.activity.MainMenu;
import com.example.drivercar.driverCarPanel.ProfileEditDriver;
import com.example.drivercar.driverCar_fagment.DriverHomeFragment;
import com.example.drivercar.driverCar_fagment.DriverOrderFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class DriverPanel_BottomNavigation extends AppCompatActivity {
    Fragment fragmenthientai;
    FrameLayout layoutHost;


    private TextView nameTv, tabProductsTv, tabOrderTv;
    private ImageButton logoutbtn;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    CircularImageView profileIv;
    String RandomUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_driver_panel__bottom_navigation);


        BottomNavigationView navigationView = (BottomNavigationView) findViewById(R.id.bottom_view);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        layoutHost = findViewById(R.id.frame_container);
        fragmenthientai = new DriverHomeFragment();
        loadFragment(fragmenthientai);


        nameTv = (TextView) findViewById(R.id.nameTv);


        tabProductsTv = (TextView) findViewById(R.id.tabProductsTv);
        tabOrderTv = (TextView) findViewById(R.id.tabOrderTv);

        logoutbtn = (ImageButton) findViewById(R.id.logoutBTN);

        profileIv = (CircularImageView) findViewById(R.id.profileIv);

        progressDialog = new ProgressDialog(DriverPanel_BottomNavigation.this);
        progressDialog.setTitle("Tình hình mạng yếu");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //make offline
                //sign out
                //go to login activity
                makeMeOffline();
            }
        });




        tabProductsTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load product
                showProductUI();
                fragmenthientai = new DriverHomeFragment();
                loadFragment(fragmenthientai);
            }
        });

        tabOrderTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //load orders
                showOrdersUI();
                fragmenthientai = new DriverOrderFragment();
                loadFragment(fragmenthientai);
            }
        });

    }

    private void showProductUI() {
        //show products ui and hide orders ui
        tabProductsTv.setTextColor(getResources().getColor(R.color.black));
        tabProductsTv.setBackgroundResource(R.drawable.shape_rect04);

        tabOrderTv.setTextColor(getResources().getColor(R.color.white));
        tabOrderTv.setBackgroundResource(R.drawable.shape_rect03);
    }

    private void showOrdersUI() {
        //show orders ui and hide products ui
        tabProductsTv.setTextColor(getResources().getColor(R.color.white));
        tabProductsTv.setBackgroundResource(R.drawable.shape_rect03);

        tabOrderTv.setTextColor(getResources().getColor(R.color.black));
        tabOrderTv.setBackgroundResource(R.drawable.shape_rect04);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.chefHome :
                    fragment=new DriverHomeFragment();
                    break;
                case R.id.tabProductsTv :
                    fragment=new DriverHomeFragment();
                    break;
                case R.id.chefPendingOrders:
                    fragment=new DriverPendingOrderFragment();
                    break;
                case R.id.chef_Orders :
                    fragment=new DriverOrderFragment();
                    break;
                case R.id.tabOrderTv:
                    fragment=new DriverOrderFragment();
                    break;
                case R.id.chef_Profile:
                    fragment=new DriverProfileFragment();
                    break;
            }
            return loadcheffragment(fragment);
        }
    };



    private boolean loadcheffragment(Fragment fragment) {

        if (fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,fragment).commit();
            return true;
        }
        return false;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.commit();
    }

    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null)
        {
            startActivity(new Intent(DriverPanel_BottomNavigation.this, MainMenu.class));
            finish();
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
                            String nameshop = ""+ds.child("NameShop").getValue();
                            String email = ""+ ds.child("EmailId").getValue();
                            String profile = ""+ds.child("ImageURL").getValue();

                            nameTv.setText(""+fname+" "+name);



                            try {
                                Picasso.get().load(profile).placeholder(R.drawable.ic_camera_24).into(profileIv);
                            }catch (Exception e)
                            {
                                profileIv.setImageResource(R.drawable.ic_camera_24);
//                                Toasty.error(ChefFoodPanel_BottomNavigation.this, ""+e.getMessage(), Toast.LENGTH_SHORT, true).show();

                            }
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
                        Toast.makeText(DriverPanel_BottomNavigation.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
