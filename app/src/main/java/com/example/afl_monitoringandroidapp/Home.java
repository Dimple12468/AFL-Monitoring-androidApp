package com.example.afl_monitoringandroidapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    private TextView user;
    private Button logOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        user = findViewById(R.id.user);
//        logOut = findViewById(R.id.logOut);

        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        String typeofuser = preferences.getString("role", "");

        if (typeofuser.equals("1"))
            Toast.makeText(Home.this, "login for farmer", Toast.LENGTH_SHORT).show();
        if (typeofuser.equals("2"))
            user.setText("ADO USER");
        if (typeofuser.equals("3"))
            Toast.makeText(this, "login for block admin", Toast.LENGTH_SHORT).show();
        if (typeofuser.equals("4"))
            user.setText("DDA USER");
        if (typeofuser.equals("5"))
            user.setText("ADMIN USER");
        if (typeofuser.equals("6"))
            Toast.makeText(this, "login for super admin", Toast.LENGTH_SHORT).show();

//        logOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Home.this,LoginActivity.class);
//                startActivity(intent);
//            }
//        });
    }



}