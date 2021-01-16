package com.example.afl_monitoringandroidapp.statusTabs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.afl_monitoringandroidapp.Globals;
import com.example.afl_monitoringandroidapp.R;

public class completed extends Fragment {

    private String statusURL = Globals.completedDatewiseList;

    public completed() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_completed, container, false);
        TextView textView = root.findViewById(R.id.statusCompleted);
        textView.setText("This is completed fragment.");
        return root;
    }
}