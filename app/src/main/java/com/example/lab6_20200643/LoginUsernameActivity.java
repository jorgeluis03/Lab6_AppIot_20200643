package com.example.lab6_20200643;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.lab6_20200643.databinding.ActivityLoginUsernameBinding;
import com.example.lab6_20200643.model.UserModel;
import com.example.lab6_20200643.util.FirebaseUtil;
import com.google.firebase.Timestamp;

public class LoginUsernameActivity extends AppCompatActivity {
    ActivityLoginUsernameBinding binding;
    EditText usernameLogin;
    Button entrarBtn;
    ProgressBar progressBar;
    String phoneUser;
    UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginUsernameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        usernameLogin = binding.loginUsername;
        entrarBtn = binding.loginEntrarBtn;
        progressBar = binding.loginProgressBar;

        phoneUser = getIntent().getStringExtra("phone");
        getUserName();
        setInProgress(false);

        entrarBtn.setOnClickListener(view -> {
            String username = usernameLogin.getText().toString();
            guardarUser(username);
        });



    }
    public void guardarUser(String username){
        if(username.isEmpty() || username.length()<3){
            usernameLogin.setError("El nombre de usuario debe tener más de 3 caracteres");
            return;
        }

        setInProgress(true);
        if (userModel!=null){
            userModel.setUsername(username);
        }else {
            userModel = new UserModel(phoneUser,username, Timestamp.now(), FirebaseUtil.actualUserId());
        }

        FirebaseUtil.currentUserDetails().set(userModel)
                .addOnCompleteListener(task -> {
                    setInProgress(false);
                    if(task.isSuccessful()){
                        Intent intent = new Intent(LoginUsernameActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
    }
    public void getUserName(){
        setInProgress(true);
        //Obtengo el documento del usuario pro el id de autenticacion
        FirebaseUtil.currentUserDetails().get()
                .addOnCompleteListener(task -> {
                    setInProgress(false);
                    if (task.isSuccessful()){
                        userModel = task.getResult().toObject(UserModel.class);

                        //siempre valida
                        if(userModel!=null){
                            usernameLogin.setText(userModel.getUsername());
                        }
                    }
                });
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