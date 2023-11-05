package com.example.lab6_20200643;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.lab6_20200643.databinding.ActivityLoginUsernameBinding;

public class LoginUsernameActivity extends AppCompatActivity {
    ActivityLoginUsernameBinding binding;
    EditText usernameLogin;
    Button entrarBtn;
    ProgressBar progressBar;
    String phoneUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginUsernameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usernameLogin = binding.loginUsername;
        entrarBtn = binding.loginEntrarBtn;
        progressBar = binding.loginProgressBar;

        phoneUser = getIntent().getStringExtra("phone");

        setInProgress(false);

        entrarBtn.setOnClickListener(view -> {
            String username = usernameLogin.getText().toString();
            guardarUser(username);
        });


    }
    public void guardarUser(String username){

    }
    public void setInProgress(boolean isProgress){
        if(isProgress){
            progressBar.setVisibility(View.VISIBLE);
            entrarBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            entrarBtn.setVisibility(View.VISIBLE);
        }
    }
}