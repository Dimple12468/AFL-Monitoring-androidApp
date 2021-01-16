package com.example.afl_monitoringandroidapp;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.afl_monitoringandroidapp.statusTabs.completed;
import com.example.afl_monitoringandroidapp.statusTabs.ongoing;
import com.example.afl_monitoringandroidapp.statusTabs.pending;

public class TabPageAdapter extends FragmentPagerAdapter {

    private int tabcount;
    private Context mContext;

    public TabPageAdapter(FragmentManager fm, int tabcount, Context context) {
        super(fm);
        this.tabcount = tabcount;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position){
            case 0:
                fragment = new pending();
                break;
            case 1:
//                Toast.makeText(mContext, "ongoing tab clicked", Toast.LENGTH_SHORT).show();
                fragment = new ongoing();
                break;
            case 2:
//                Toast.makeText(mContext, "completed tab clicked", Toast.LENGTH_SHORT).show();
                fragment = new completed();
                break;
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
