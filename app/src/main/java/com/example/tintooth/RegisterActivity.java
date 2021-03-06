package com.example.tintooth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private Button mRegister;
    private TextView existing;
    private ProgressBar spinner;
    private EditText mEmail, mPassword, mName, mPhone;
    private RadioGroup mRadioGroup;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthStateListener;
    private String emailPattern = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?";
    private static final String TAG = "RegisterActivity";

    private void bindingView(){
        spinner =findViewById(R.id.pBar);
        spinner.setVisibility(View.GONE);
        existing = findViewById(R.id.existing);
        mRegister = findViewById(R.id.register);
        mEmail =  findViewById(R.id.email);
        mPassword =  findViewById(R.id.password);
        mPhone =  findViewById(R.id.phone_number);
        mName =  findViewById(R.id.name);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        bindingView();



        mAuth = FirebaseAuth.getInstance();

        final CheckBox checkBox =  findViewById(R.id.checkbox1);
        TextView textview =  findViewById(R.id.textView2);
        checkBox.setText("");
        textview.setText(Html.fromHtml("I have read and agreed to the "+"<a href = 'https://www.blogger.com/blog/post/edit/preview/5691680832129275873/394601829011768046'> Terms and Conditions</a>"));
        textview.setClickable(true);
        textview.setMovementMethod(LinkMovementMethod.getInstance());
        existing.setOnClickListener(v -> {
            spinner.setVisibility(View.VISIBLE);
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
        });
        mRegister.setOnClickListener(v -> {
            spinner.setVisibility(View.VISIBLE);
            final String email = mEmail.getText().toString();
            final String password = mPassword.getText().toString();
            final String name = mName.getText().toString();
            final String phone = mPhone.getText().toString();
            final Boolean tnc = checkBox.isChecked();
            if(checkInputs(email, name, password, tnc)){
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }else{
                            mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Registration successful, please check your email for verification.", Toast.LENGTH_SHORT).show();
                                        String userId = mAuth.getCurrentUser().getUid();
                                        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                                        Map userInfo = new HashMap<>();
                                        userInfo.put("name", name);
                                        userInfo.put("phone", phone);
                                        userInfo.put("gender", "Others");
                                        userInfo.put("description", "");
                                        userInfo.put("profileImageUrl", "default");
                                        currentUserDb.updateChildren(userInfo);
                                        mEmail.setText("");
                                        mName.setText("");
                                        mPassword.setText("");
                                        Intent i = new Intent(RegisterActivity.this, Choose_Login_And_Reg.class);
                                        startActivity(i);
                                        finish();
                                        return;
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
            spinner.setVisibility(View.GONE);
        });
    }
    private boolean checkInputs(String email, String username, String password, Boolean tnc){
        if(email.equals("")||username.equals("")||password.equals("")){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!email.matches(emailPattern)){
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!tnc){
            Toast.makeText(this, "Please accept Terms and Conditions", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(RegisterActivity.this, Choose_Login_And_Reg.class);
        startActivity(i);
        finish();
    }
}