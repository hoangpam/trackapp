package com.example.drivercar.bottomnavigation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.example.drivercar.R;
import com.example.drivercar.activity.MainMenu;
import com.example.drivercar.driverCarPanel.DriverHomeFragment;
import com.example.drivercar.driverCar_fagment.DriverOrderFragment;
import com.example.drivercar.driverCarPanel.DriverPendingOrderFragment;
import com.example.drivercar.driverCarPanel.DriverProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DriverPanel_BottomNavigation extends AppCompatActivity {
    Fragment fragmenthientai;
    FrameLayout layoutHost;

    RelativeLayout relativeLayout;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    CircularImageView profileIv;


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
        fragmenthientai = new DriverPendingOrderFragment();
        loadFragment(fragmenthientai);

        relativeLayout = (RelativeLayout) findViewById(R.id.toolbarRl);

        profileIv = (CircularImageView) findViewById(R.id.profileIv);

        progressDialog = new ProgressDialog(DriverPanel_BottomNavigation.this);
        progressDialog.setTitle("Tình hình mạng yếu");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();

    }



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.driver_Order :
                    fragment=new DriverPendingOrderFragment();
                    break;

                case R.id.driver_my_seft:
                    fragment=new DriverHomeFragment();
                    break;
                case R.id.driver_notification :
                    fragment=new DriverOrderFragment();
                    break;

                case R.id.driver_Profile:
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





}
