package com.example.drivercar.adapter_Driver;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.drivercar.driverCar_fagment.ApCarsFragment;
import com.example.drivercar.driverCar_fagment.CompleFragment;
import com.example.drivercar.driverCar_fagment.LoadingFragment;
import com.example.drivercar.driverCar_fagment.MySeftFragment;
import com.example.drivercar.driverCar_fagment.QuotedFragment;

public class AdapterFunPenOrder extends FragmentPagerAdapter {
    private Context myContext;
    int totalTabs;

    public AdapterFunPenOrder(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:
                LoadingFragment loadingFragment = new LoadingFragment();
                return loadingFragment;
            case 1:
                QuotedFragment quotedFragment = new QuotedFragment();
                return quotedFragment;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
