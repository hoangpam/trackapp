package com.example.drivercar.adapter_Driver;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.drivercar.driverCarPanel.LoadingFragment;
import com.example.drivercar.driverCar_fagment.QuotedFragment;

public class AdapterFunPenOrder extends FragmentStatePagerAdapter {
    private Context myContext;
    int totalTabs;

    public AdapterFunPenOrder(Context context, FragmentManager fm, int totalTabs) {
        super(fm,totalTabs);
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
                return new LoadingFragment();
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
