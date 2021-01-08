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

public class adminAdo extends Fragment {

    private adminAdoViewModel adminAdoViewModel;

    public adminAdo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adminAdoViewModel = new ViewModelProvider(this).get(adminAdoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_ado, container, false);
        final TextView textView = root.findViewById(R.id.adm_ado);
        adminAdoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}