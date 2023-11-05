package com.example.lab6_20200643;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.lab6_20200643.databinding.ActivityLoginPhoneBinding;

public class LoginPhoneActivity extends AppCompatActivity {
    ActivityLoginPhoneBinding binding;
    EditText phoneNumeberLogin;
    Button registrarBtn;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginPhoneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        phoneNumeberLogin = binding.loginPhoneNumber;
        registrarBtn = binding.registrarBtn;
        progressBar = binding.loginProgressBar;

        setInProgress(false);

        registrarBtn.setOnClickListener(view -> {
            setInProgress(true);
            String phoneNumber = phoneNumeberLogin.getText().toString();
            Intent intent = new Intent(LoginPhoneActivity.this,LoginUsernameActivity.class);
            intent.putExtra("phone",phoneNumber);
            startActivity(intent);
        });



    }
    public void setInProgress(boolean isProgress){
        if(isProgress){
            progressBar.setVisibility(View.VISIBLE);
            registrarBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            registrarBtn.setVisibility(View.VISIBLE);
        }
    }
}