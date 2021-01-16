package com.example.afl_monitoringandroidapp.adminTabs;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.afl_monitoringandroidapp.R;
import com.example.afl_monitoringandroidapp.TabPageAdapter;
import com.google.android.material.tabs.TabLayout;

public class adminLocations extends Fragment {

    private adminLocationViewModel adminLocationViewModel;
    private TabPageAdapter mpageAdapter;
    private ViewPager pager;
    private TabLayout mtablayout;

    public adminLocations() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        adminLocationViewModel = new ViewModelProvider(this).get(adminLocationViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_locations, container, false);
        mtablayout = root.findViewById(R.id.tabLayout);
        pager = root.findViewById(R.id.viewPager);

        TextView title_top = root.findViewById(R.id.topTitleName);
        if (root.isEnabled()){
            title_top.setText(R.string.admin_locations);
        }else {
            title_top.setText(R.string.app_name);
        }

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.appTitleBarLoc);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        mpageAdapter = new TabPageAdapter(getChildFragmentManager(),mtablayout.getTabCount(),getContext());
        pager.setAdapter(mpageAdapter);
//        mtablayout.setupWithViewPager(pager);

        mtablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mtablayout));
        pager.setOffscreenPageLimit(3);



//        final TextView textView = root.findViewById(R.id.loc);
//        adminLocationViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        return root;
    }
}