package com.example.tintooth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity  extends AppCompatActivity {
    private EditText usernameEdt,passedit,emailEdt;
    private RadioGroup ratioGroup;
    private RadioButton radio;
    private Button btnregister;
    private FirebaseAuth mAuth;
    void bindingView(){
        usernameEdt =findViewById(R.id.username);
        passedit = findViewById(R.id.password);
        btnregister = findViewById(R.id.btnregister);
        emailEdt = findViewById(R.id.email);
        ratioGroup = findViewById(R.id.ratioGroup);
        int selectId = ratioGroup.getCheckedRadioButtonId();
        radio = findViewById(selectId);
    }
    void bindingAction(){
        btnregister.setOnClickListener(this:: btnregister);
    }
    private void btnregister(View view) {
        register();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        bindingView();
        bindingAction();
    }

    private void register() {

        String username,pass, email;
        username = usernameEdt.getText().toString();
        pass = passedit.getText().toString();
        email = emailEdt.getText().toString();

        if(radio.getText()==null){
            return;
        }

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this,"Vui long nhap Email!!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Vui long nhap Email!!",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String userId = mAuth.getCurrentUser().getUid();
                    DatabaseReference currentUserDb =
                            FirebaseDatabase.getInstance().getReference()
                                    .child("Users")
                                    .child(radio.getText().toString())
                                    .child(userId)
                                    .child("name");
                    currentUserDb.setValue(username);

                    Toast.makeText(getApplicationContext(),"Tao tài khoản thanh cong !!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(),"Register không thanh cong !!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
