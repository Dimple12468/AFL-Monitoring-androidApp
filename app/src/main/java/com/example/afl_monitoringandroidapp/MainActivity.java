package com.example.afl_monitoringandroidapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String typeofuser = preferences.getString("role","");
                String username = preferences.getString("Name","");
                Intent i;
                if(typeofuser.isEmpty()){
                    i = new Intent(MainActivity.this ,InitialPage.class);
                }else
                    i = new Intent(MainActivity.this ,LoginActivity.class);

//                Intent i;
//                 i = new Intent( MainActivity.this ,InitialPage.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
