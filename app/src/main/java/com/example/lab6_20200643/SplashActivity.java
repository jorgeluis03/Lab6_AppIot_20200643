package com.example.lab6_20200643;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.lab6_20200643.util.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(() -> {
            if(FirebaseUtil.userEstaLogeado()){
                startActivity(new Intent(SplashActivity.this,MainActivity.class));
            }else {
                startActivity(new Intent(SplashActivity.this,LoginPhoneActivity.class));
            }

            finish();
        },1000);
    }
}