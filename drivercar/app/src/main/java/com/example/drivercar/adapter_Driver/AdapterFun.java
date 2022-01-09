package com.example.drivercar.adapter_Driver;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.drivercar.driverCar_fagment.ApCarsFragment;
import com.example.drivercar.driverCar_fagment.CompleFragment;
import com.example.drivercar.driverCarPanel.LoadingFragment;
import com.example.drivercar.driverCarPanel.MySeftFragment;

public class AdapterFun extends FragmentStatePagerAdapter {
    private Context myContext;
    int totalTabs;

    public AdapterFun(Context context, FragmentManager fm, int totalTabs) {
        super(fm,totalTabs);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ApCarsFragment apCarsFragment = new ApCarsFragment();
                return apCarsFragment;
            case 1:
                CompleFragment compleFragment = new CompleFragment();
                return compleFragment;
            case 2:
                MySeftFragment mySeftFragment = new MySeftFragment();
                return mySeftFragment;
            default:
                return new ApCarsFragment();
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
