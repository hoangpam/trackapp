package com.example.drivercar.driverCarPanel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.drivercar.R;
import com.example.drivercar.adapter_Driver.AdapterFun;
import com.example.drivercar.adapter_Driver.AdapterFunPenOrder;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import es.dmoral.toasty.Toasty;

public class DriverPendingOrderFragment extends Fragment {

    private TabLayout TabTab;
    private TabItem TabApCars,TabQuoted;
    private ViewPager viewPager;
    private ImageButton call_urgent_Btn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_pendingorders,null);
        TabTab = (TabLayout) v.findViewById(R.id.TabTab);
        TabApCars = (TabItem) v.findViewById(R.id.TabApCars);
        TabQuoted = (TabItem) v.findViewById(R.id.TabQuoted);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        call_urgent_Btn = v.findViewById(R.id.call_urgent_Btn);


        call_urgent_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialPhone();
            }
        });
        final AdapterFunPenOrder adapter = new AdapterFunPenOrder(getContext(),getParentFragmentManager(), TabTab.getTabCount());
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
    String phone1 = "1800199000";

    private void dialPhone() {

        startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+Uri.encode(phone1))));
        Toasty.info(getContext(), ""+phone1, Toast.LENGTH_SHORT, true).show();

    }


}
