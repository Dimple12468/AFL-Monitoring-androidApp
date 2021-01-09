package com.example.afl_monitoringandroidapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {

//    private TextView user;
    private String whichUser;
    private BottomNavigationView navView;
    AppBarConfiguration appBarConfiguration;
    NavController navController;

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

        navView = findViewById(R.id.nav_view);

        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        String typeofuser = preferences.getString("role", "");

        switch (typeofuser) {
            case "1":
                Toast.makeText(Home.this, "login for farmer", Toast.LENGTH_SHORT).show();
                break;
            case "2":
                navView.inflateMenu(R.menu.ado_bottom_nav);
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.home_ado, R.id.pending_ado, R.id.completed_ado)
                        .build();
                navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                navController.setGraph(R.navigation.mobile_navigation_ado);
                NavigationUI.setupWithNavController(navView, navController);
                break;
            case "3":
                Toast.makeText(this, "login for block admin", Toast.LENGTH_SHORT).show();
                break;
            case "4":
                navView.inflateMenu(R.menu.dda_bottom_nav);
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.home_dda, R.id.pending_dda, R.id.ongoing_dda, R.id.completed_dda, R.id.ado_dda)
                        .build();
                navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                navController.setGraph(R.navigation.mobile_navigation_dda);
                NavigationUI.setupWithNavController(navView, navController);
                break;
            case "5":
//            navView = findViewById(R.id.nav_view);
                navView.inflateMenu(R.menu.admin_bottom_nav);
                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.adminHome, R.id.adminLocation, R.id.adminAdo, R.id.adminDda, R.id.adminStats)
                        .build();
                navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                navController.setGraph(R.navigation.mobile_navigation_admin);
//            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
                NavigationUI.setupWithNavController(navView, navController);
                break;
            case "6":
                Toast.makeText(this, "login for super admin", Toast.LENGTH_SHORT).show();
                break;
        }


    }

    void callAdminFragments(String userType){

    }



}