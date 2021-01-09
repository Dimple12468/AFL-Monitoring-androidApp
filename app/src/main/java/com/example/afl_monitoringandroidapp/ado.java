package com.example.afl_monitoringandroidapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ado extends Fragment {

    public ado() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ado, container, false);
        TextView textView = root.findViewById(R.id.ddaAdo);
        textView.setText("This is ado fragment");
        return root;
    }
}