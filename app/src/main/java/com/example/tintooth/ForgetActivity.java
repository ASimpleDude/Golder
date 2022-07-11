package com.example.tintooth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgetActivity extends AppCompatActivity {

    private Button mForgetPasswordButton;
    private EditText mEmail;
    private FirebaseAuth mAuth;
    private int flag;
    private String EMAIL_PATTERN="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        mAuth = FirebaseAuth.getInstance();
        flag = 0;
        mForgetPasswordButton = findViewById(R.id.resetPasswordButton);
        mEmail = findViewById(R.id.resetPasswordEmail);

        mForgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        flag = 1;
                        mAuth.sendPasswordResetEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ForgetActivity.this, "Password reset instructions is send to your email", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ForgetActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                if(flag == 0){
                    Toast.makeText(ForgetActivity.this, "Email address not found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent btnClick = new Intent(ForgetActivity.this, LoginActivity.class);
        startActivity(btnClick);
    }
}