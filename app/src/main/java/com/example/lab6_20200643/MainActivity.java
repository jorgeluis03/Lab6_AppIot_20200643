package com.example.lab6_20200643;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.lab6_20200643.databinding.ActivityMainBinding;
import com.example.lab6_20200643.util.FirebaseUtil;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    ImageButton logoutBtn;
    ImageButton juego1;
    ImageButton juego2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        logoutBtn = binding.logoutBtn;
        juego1 = binding.juego1;
        juego2 = binding.juego2;

        juego1.setOnClickListener(v -> {
            startActivity(new Intent(this, Juego1Activity.class));
        });

        juego2.setOnClickListener(v -> {
            startActivity(new Intent(this, Juego2Activity.class));
        });


        logoutBtn.setOnClickListener(v -> {
            FirebaseUtil.logout();
            Intent intent = new Intent(this, SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}