package com.example.afl_monitoringandroidapp.statusTabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.afl_monitoringandroidapp.InitialPage;
import com.example.afl_monitoringandroidapp.R;

public class pending extends Fragment {


    public pending() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_pending, container, false);
        TextView textView = root.findViewById(R.id.statusPending);
        textView.setText("This is pending fragment");

        Button button = root.findViewById(R.id.logOut_Pending);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(), InitialPage.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return root;
    }
}