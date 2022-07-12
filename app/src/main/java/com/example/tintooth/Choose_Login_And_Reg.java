package com.example.tintooth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class Choose_Login_And_Reg extends AppCompatActivity {

    private Button mLogin, mRegister;
    private ProgressBar pBar;
    private void bindingView(){
        mLogin = findViewById(R.id.login);
        mRegister = findViewById(R.id.register);
        pBar = findViewById(R.id.pBar);
        pBar.setVisibility(View.GONE);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_and_reg);
        bindingView();
        mLogin.setOnClickListener(v -> {
            pBar.setVisibility(View.VISIBLE);
            Intent i = new Intent(Choose_Login_And_Reg.this, LoginActivity.class);
            startActivity(i);
        });
        mRegister.setOnClickListener(v -> {
            pBar.setVisibility(View.VISIBLE);
            Intent i = new Intent(Choose_Login_And_Reg.this, RegisterActivity.class);
            startActivity(i);
        });
    }
}