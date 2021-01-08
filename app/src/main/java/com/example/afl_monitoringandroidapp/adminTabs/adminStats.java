package com.example.afl_monitoringandroidapp.adminTabs;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.afl_monitoringandroidapp.R;

public class adminStats extends Fragment {

    private adminStatsViewModel adminStatsViewModel;

    public adminStats() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adminStatsViewModel = new ViewModelProvider(this).get(adminStatsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_stats, container, false);
        final TextView textView = root.findViewById(R.id.stats);
        adminStatsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}