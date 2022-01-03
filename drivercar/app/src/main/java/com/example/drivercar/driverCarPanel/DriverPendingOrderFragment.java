package com.example.drivercar.driverCarPanel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.drivercar.R;
import com.example.drivercar.adapter_Driver.AdapterFun;
import com.example.drivercar.adapter_Driver.AdapterFunPenOrder;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class DriverPendingOrderFragment extends Fragment {

    private TabLayout TabTab;
    private TabItem TabApCars,TabQuoted;
    private ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_driver_pendingorders,null);
        TabTab = (TabLayout) v.findViewById(R.id.TabTab);
        TabApCars = (TabItem) v.findViewById(R.id.TabApCars);
        TabQuoted = (TabItem) v.findViewById(R.id.TabQuoted);
        viewPager = (ViewPager) v.findViewById(R.id.viewPager);
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


}
