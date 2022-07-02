package com.example.tintooth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingsActivity extends AppCompatActivity {
    private EditText mNameField, mPhoneField, mBudget;
    private ProgressBar spinner;
    private Button mConfirm;
    private ImageButton mBack;
    private ImageView mProfileImage;
    private Spinner need,give;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userId, name, phone, profileImageUrl, userSex, userBudget, userNeed, userGive;
    private int needIndex, giveIndex;
    private Uri resultUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        spinner = (ProgressBar) findViewById(R.id.pBar);
        spinner.setVisibility(View.GONE);

        mNameField = (EditText) findViewById(R.id.name);
        mPhoneField = (EditText) findViewById(R.id.phone);

        mProfileImage = (ImageView) findViewById(R.id.profileImage);
        mBack = findViewById(R.id.settingsBack);

        mConfirm = (Button) findViewById(R.id.confirm);
        mBudget = (EditText) findViewById(R.id.budget_settings);
        need = (Spinner) findViewById(R.id.spinner_need_settings);
        give = (Spinner) findViewById(R.id.spinner_give_setting);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null && mAuth.getCurrentUser() != null)
            userId = mAuth.getCurrentUser().getUid();
        else {
            finish();
        }
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(userId);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.services,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        need.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter_give = ArrayAdapter.createFromResource(this, R.array.services,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        give.setAdapter(adapter_give);

        //get User Info();
        mProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkPermission()){
                    Toast.makeText(SettingsActivity.this, "Allow access to continue!", Toast.LENGTH_SHORT).show();
                    requestPermissions();
                }
                else{
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent,1);
                }
            }
        });
    }

}