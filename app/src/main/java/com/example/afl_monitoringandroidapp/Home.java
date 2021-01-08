package com.example.afl_monitoringandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

//    private TextView user;
    private String whichUser;
    private BottomNavigationView navView;

    public Home() {
        // Required empty public constructor
    }

    public Home(String typeOfUser) {
        whichUser = typeOfUser;
        callAdminFragments(whichUser);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

//        user = findViewById(R.id.user);


        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        String typeofuser = preferences.getString("role", "");

        if (typeofuser.equals("1"))
            Toast.makeText(Home.this, "login for farmer", Toast.LENGTH_SHORT).show();
        if (typeofuser.equals("2"))
            Toast.makeText(Home.this, "login for ado", Toast.LENGTH_SHORT).show();
        if (typeofuser.equals("3"))
            Toast.makeText(this, "login for block admin", Toast.LENGTH_SHORT).show();
        if (typeofuser.equals("4"))
            Toast.makeText(Home.this, "login for dda", Toast.LENGTH_SHORT).show();
        if (typeofuser.equals("5")) {
//            Toast.makeText(Home.this, "login for admin", Toast.LENGTH_SHORT).show();
            navView = findViewById(R.id.nav_view);
            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.adminHome, R.id.adminLocation, R.id.adminAdo,R.id.adminDda,R.id.adminStats)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);
        }
        if (typeofuser.equals("6"))
            Toast.makeText(this, "login for super admin", Toast.LENGTH_SHORT).show();


    }

    void callAdminFragments(String userType){

    }



}