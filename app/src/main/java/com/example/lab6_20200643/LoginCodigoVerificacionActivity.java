package com.example.lab6_20200643;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab6_20200643.databinding.ActivityLoginCodigoVerificacionBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginCodigoVerificacionActivity extends AppCompatActivity {
    FirebaseFirestore db;
    ActivityLoginCodigoVerificacionBinding binding;
    String phoneNumber;
    EditText ocodigoInput;
    Button nextBtn;
    ProgressBar progressBar;
    TextView reenviarCodigoTextView;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Long timeoutSeconds = 60L;
    String codigoVerification;
    PhoneAuthProvider.ForceResendingToken reenviarToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginCodigoVerificacionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ocodigoInput = binding.loginOtp;
        nextBtn = binding.loginNextBtn;
        progressBar = binding.loginProgressBar;
        reenviarCodigoTextView = binding.resendOtpTextView;

        phoneNumber = getIntent().getStringExtra("phone");

        enviarCodigoVerificacion(phoneNumber,false);

        nextBtn.setOnClickListener(v -> {
            String enterCodigoVerif = ocodigoInput.getText().toString();

            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codigoVerification, enterCodigoVerif);
            logearse(credential);
        });


    }
    public void enviarCodigoVerificacion(String phoneNumber, boolean pressReenviar){
        startResendTimer();
        setInProgress(true);
        //la autenticaciones la haremos con credenciales de codigo de verificacion
        PhoneAuthOptions.Builder builder = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        logearse(phoneAuthCredential);
                        setInProgress(false);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(LoginCodigoVerificacionActivity.this, "La verificación falló", Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                    }
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        codigoVerification = s;
                        reenviarToken = forceResendingToken;
                        Toast.makeText(LoginCodigoVerificacionActivity.this, "Código enviado", Toast.LENGTH_SHORT).show();
                        setInProgress(false);
                    }
                });
        if(pressReenviar){
            PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(reenviarToken).build());
        }else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build());
        }
    }
    public void logearse(PhoneAuthCredential phoneAuthCredential){
        //login y luego ir a la actividad

        setInProgress(true);
        auth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(task -> {
                    setInProgress(false);
                    if (task.isSuccessful()){
                        Intent intent = new Intent(LoginCodigoVerificacionActivity.this,LoginUsernameActivity.class);
                        intent.putExtra("phone",phoneNumber);
                        startActivity(intent);
                    }else {
                        Toast.makeText(this, "Verificacion OTP falló", Toast.LENGTH_SHORT).show();
                    }

                });


    }
    public void setInProgress(boolean isProgress){
        if(isProgress){
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }
    public void startResendTimer(){
        reenviarCodigoTextView.setEnabled(false);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutSeconds--;
                reenviarCodigoTextView.setText("Reenviar código en "+timeoutSeconds+" segundos");
                if(timeoutSeconds<=0){
                    timeoutSeconds = 60L;
                    timer.cancel();
                    runOnUiThread(() -> {
                        reenviarCodigoTextView.setEnabled(true);
                    });
                }
            }
        },0,1000);
    }
}