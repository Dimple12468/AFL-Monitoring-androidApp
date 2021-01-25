package com.example.afl_monitoringandroidapp;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.afl_monitoringandroidapp.statusTabs.commonStatusTab;

public class TabPageAdapter extends FragmentPagerAdapter {

    private int tabcount;
    private Context mContext;
    private int mPosition;

    public TabPageAdapter(FragmentManager fm, int tabcount, Context context) {
        super(fm);
        this.tabcount = tabcount;
        this.mContext = context;
    }


    public TabPageAdapter(FragmentManager childFragmentManager, Context context) {
        super(childFragmentManager);
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {

        return commonStatusTab.newInstance(position + 1);

//        Fragment fragment = null;
//        switch(position){
//            case 0:
//                fragment = new completed();
//                mPosition = 0;
//                break;
//            case 1:
////                Toast.makeText(mContext, "ongoing tab clicked", Toast.LENGTH_SHORT).show();
//                fragment = new completed();
//                mPosition = 1;
//                break;
//            case 2:
////                Toast.makeText(mContext, "completed tab clicked", Toast.LENGTH_SHORT).show();
//                fragment = new completed();
//                mPosition = 2;
//                break;
//        }
//
//        return fragment;
    }

    @Override
    public int getCount() {
        return tabcount;
    }

//    public int getPosition() {
//        return mPosition;
//    }
}
