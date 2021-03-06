package com.example.bookingcar.bottomnavigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.example.bookingcar.R;
import com.example.bookingcar.activity.MainMenu;
import com.example.bookingcar.customerCar_fagment.CustomerCartFragmnet;
import com.example.bookingcar.customerCar_fagment.CustomerHomeFragment;
import com.example.bookingcar.customerCar_fagment.CustomerOrdersFragment;
import com.example.bookingcar.customerCar_fagment.CustomerProfileFragment;
import com.example.bookingcar.customerCar_fagment.CutomerTrackFragment;
import com.example.bookingcar.model.ModeDriver;
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

import java.util.ArrayList;
import java.util.HashMap;

public class CustomerPanel_BottomNavigation extends AppCompatActivity {
    RelativeLayout toolbarRl;
    Fragment fragmenthientai;
    FrameLayout layoutHost;
    private ProgressDialog progressDialog;


    private FirebaseAuth firebaseAuth;

    private ArrayList<ModeDriver> driverList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_panel__bottom_navigation);

        BottomNavigationView navigationView = findViewById(R.id.bottom_navigation);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigationView.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());
        layoutHost = findViewById(R.id.fragment_container);
        fragmenthientai = new CustomerHomeFragment();
        loadFragment(fragmenthientai);


        progressDialog = new ProgressDialog(CustomerPanel_BottomNavigation.this);
        progressDialog.setTitle("T??nh h??nh m???ng y???u");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();








    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        //hi???n th??? c??c d??ng menu n???m ph??a d?????i
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.cus_order:
                    fragment=new CustomerHomeFragment();
                    break;
            }
            switch (item.getItemId()){
                case R.id.cus_endow:
                    fragment=new CustomerCartFragmnet();
                    break;
            }
            switch (item.getItemId()){
                case R.id.cus_add:
                    fragment = new CutomerTrackFragment();
                    break;
            }
            switch (item.getItemId()){
                case R.id.cust_profile:
                    fragment=new CustomerProfileFragment();
                    break;
            }
            switch (item.getItemId()){
                case R.id.cus_notification:
                    fragment=new CustomerOrdersFragment();
                    break;
            }

            return loadcheffragment(fragment);

        }
    };

    //h??m load c??c fragment
    private boolean loadcheffragment(Fragment fragment) {

        if(fragment != null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            return true;
        }
        return false;
    }

    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    //????? d??? li???u cho textview ng?????i d??ng
    //toolbar
    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null)
        {
            startActivity(new Intent(CustomerPanel_BottomNavigation.this, MainMenu.class));
            finish();
        }
        else {
//            loadMyInfo();
        }
    }






}
