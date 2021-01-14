package com.example.afl_monitoringandroidapp.adminTabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.afl_monitoringandroidapp.InitialPage;
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

        Button button = root.findViewById(R.id.logOut_ado);
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