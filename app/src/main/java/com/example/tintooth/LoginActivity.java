package com.example.tintooth;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private ProgressBar spinner;
    private TextView existing;
    private Button mLogin;
    private EditText mEmail, mPassword;
    private TextView mForgetPassword;
    private boolean loginBtnClicked;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private void bindingView(){
        spinner = findViewById(R.id.pBar);
        existing = findViewById(R.id.existing);
        spinner.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        mLogin = findViewById(R.id.login);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password);
        mForgetPassword = findViewById(R.id.forgetPassword);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        bindingView();
        loginBtnClicked = false;

        existing.setOnClickListener(v -> {
            spinner.setVisibility(View.VISIBLE);
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });
        mLogin.setOnClickListener(v -> {
            loginBtnClicked = true;
            spinner.setVisibility(View.VISIBLE);
            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();
            if(isStringNull(email)||isStringNull(password)){
                Toast.makeText(LoginActivity.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();
            }else{
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, task -> {
                    if(!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }else{
                        if(Objects.requireNonNull(mAuth.getCurrentUser()).isEmailVerified()){
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }else{
                            Toast.makeText(LoginActivity.this, "Please verify your email",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            spinner.setVisibility(View.GONE);
        });
        mForgetPassword.setOnClickListener(v -> {
            spinner.setVisibility(View.VISIBLE);
            Intent i = new Intent(LoginActivity.this, ForgetActivity.class);
            startActivity(i);
            finish();
        });
        firebaseAuthStateListener = firebaseAuth -> {
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null && user.isEmailVerified() && !loginBtnClicked){
                spinner.setVisibility(View.VISIBLE);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
                spinner.setVisibility(View.GONE);
            }
        };
    }
    private boolean isStringNull(String email){
        return email.equals("");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(LoginActivity.this, Choose_Login_And_Reg.class);
        startActivity(i);
        finish();
    }
}