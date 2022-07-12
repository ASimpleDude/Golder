package com.example.tintooth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ForgetActivity extends AppCompatActivity {

    private Button mForgetPasswordButton;
    private EditText mEmail;
    private FirebaseAuth mAuth;
    private int flag;
    private final String EMAIL_PATTERN="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    private String email;


    private void bindingView(){
        mForgetPasswordButton = findViewById(R.id.resetPasswordButton);
        mEmail = findViewById(R.id.resetPasswordEmail);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        bindingView();
        mAuth = FirebaseAuth.getInstance();
        flag = 0;


        mForgetPasswordButton.setOnClickListener(v -> {
            email = mEmail.getText().toString();
            if (email.equals("")){
                Toast.makeText(ForgetActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            //check if email matches pattern
            if (!email.matches(EMAIL_PATTERN)){
                Toast.makeText(ForgetActivity.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                flag = 1;
                mAuth.sendPasswordResetEmail(mEmail.getText().toString()).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()){
                        Toast.makeText(ForgetActivity.this, "Password reset instructions is send to your email", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(ForgetActivity.this, Objects.requireNonNull(task1.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
            if(flag == 0){
                Toast.makeText(ForgetActivity.this, "Email address not found", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent btnClick = new Intent(ForgetActivity.this, LoginActivity.class);
        startActivity(btnClick);
    }
}