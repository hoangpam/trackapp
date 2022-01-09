package com.example.bookingcar.adapter_Customer;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.bookingcar.customerCar_fagment.LoadingRunFragment;
import com.example.bookingcar.customerCar_fagment.NewFragment;
import com.example.bookingcar.customerCar_fagment.PassedFragment;


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
                NewFragment newFragment = new NewFragment();
                return newFragment;
            case 1:
                LoadingRunFragment loadingFragment = new LoadingRunFragment();
                return loadingFragment;

            case 2:
                PassedFragment passedFragment = new PassedFragment();
                return passedFragment;
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
