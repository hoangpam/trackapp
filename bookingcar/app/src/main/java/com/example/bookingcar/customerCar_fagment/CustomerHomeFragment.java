package com.example.bookingcar.customerCar_fagment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.bookingcar.adapter_Customer.AdapterFun;
import com.example.bookingcar.model.ModeDriver;
import com.example.bookingcar.R;
import com.example.bookingcar.activity.MainMenu;
import com.example.bookingcar.adapter_Customer.AdapterDriver;
import com.example.bookingcar.model.ModeDriver;
import com.example.bookingcar.object.Constants;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

import javax.annotation.Nullable;

import es.dmoral.toasty.Toasty;

//implements SwipeRefreshLayout.OnRefreshListener
public class CustomerHomeFragment extends Fragment {

    private TabLayout TabTab;
    private TabItem Tabneworder,Tabloadingrun,Tabpassed;
    private ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_customerhome, null);
        TabTab = (TabLayout) v.findViewById(R.id.TabTab);
        Tabneworder = (TabItem) v.findViewById(R.id.Tabneworder);
        Tabloadingrun = (TabItem) v.findViewById(R.id.Tabloadingrun);
        Tabpassed = (TabItem) v.findViewById(R.id.Tabpassed);
        viewPager = v.findViewById(R.id.viewPager);

        final AdapterFun adapter = new AdapterFun(getContext(),getParentFragmentManager(), TabTab.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(TabTab));

        TabTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return v;
    }


}
