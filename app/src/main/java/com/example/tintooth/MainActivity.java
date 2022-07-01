package com.example.tintooth;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.example.tintooth.DAO.DbConnect;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private int i;
    private TextView welcome;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String username;

    private FirebaseAuth mAuth;

    private String currentUId;

    private DatabaseReference usersDb;

    private ArrayList<String> al;
    private ArrayAdapter arrayAdapter;
    private Card card_data[];
    ListView listView;
    List<Card> rowItems;

private void bindingView() {
    welcome = findViewById(R.id.welcomeEdt);
    sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
    username = sharedPreferences.getString("username", "username not found");
    welcome.setText("Hello " + username);
}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingView();


        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUId = mAuth.getCurrentUser().getUid();











    }








    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
        return;
    }


}