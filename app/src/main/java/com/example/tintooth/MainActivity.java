package com.example.tintooth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tintooth.Cards.arrayAdapter;
import com.example.tintooth.Cards.cards;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import ru.dimorinny.showcasecard.ShowCaseView;
import ru.dimorinny.showcasecard.position.ShowCasePosition;
import ru.dimorinny.showcasecard.position.ViewPosition;
import ru.dimorinny.showcasecard.radius.Radius;

public class MainActivity extends AppCompatActivity {

    boolean firstStart;
    private cards card_data[];
    private com.example.tintooth.Cards.arrayAdapter arrayAdapter;
    private ProgressBar pBar ;
    private TextView noOne;
    private DatabaseReference usersDb;
    private String currentUId;

    private List<cards> rowItem;
    private void bindingView(){
        pBar = findViewById(R.id.pBar);
        pBar.setVisibility(View.VISIBLE);
        noOne = findViewById(R.id.noOne);
        noOne.setVisibility(View.GONE);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingView();
        setupTopNavigationView();
        rowItem = new ArrayList<>();

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists() && !snapshot.child("connections").child("nope").hasChild(currentUId) && !snapshot.child("connections").child("yeps").hasChild(currentUId)){
                    pBar.setVisibility(View.GONE);
                    cards item = new cards(snapshot.getKey(), snapshot.child("name").getValue().toString(), snapshot.child("profileImageUrl").getValue().toString(), snapshot.child("gender").getValue().toString(), snapshot.child("description").getValue().toString(), snapshot.child("phone").getValue().toString());
                    rowItem.add(item);
                    if(item.getUserId().equals(currentUId)){
                        rowItem.remove(item);
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItem );

        SwipeFlingAdapterView flingContainer = findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                rowItem.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards card = (cards) dataObject;
                String userId = card.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUId).setValue(true);
                Toast.makeText(MainActivity.this, card.getName()+" Disliked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards card = (cards) dataObject;
                String userId = card.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentUId).setValue(true);
                isConnectionMatch(userId);
                Toast.makeText(MainActivity.this, card.getName()+" Liked", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int i) {

            }


            @Override
            public void onScroll(float scrollProgressPercent) {
            }
        });


        flingContainer.setOnItemClickListener((itemPosition, dataObject) -> Toast.makeText(MainActivity.this, "Click", Toast.LENGTH_SHORT).show());
    }

    private void isConnectionMatch(String userId){
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUId).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String key = FirebaseDatabase.getInstance().getReference().child("Chat").push().getKey();

                    Toast.makeText(MainActivity.this, "You have found a match!", Toast.LENGTH_LONG).show();

                    usersDb.child(snapshot.getKey()).child("connections").child("matches").child(currentUId).child("chatId").setValue(key);

                    usersDb.child(currentUId).child("connections").child("matches").child(snapshot.getKey()).child("chatId").setValue(key);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupTopNavigationView(){
        BottomNavigationViewEx tvEx = findViewById(R.id.topNavViewBar);
        TopNavigationView.setupTopNavigationView(tvEx);
        TopNavigationView.enableNavigation(MainActivity.this, tvEx);
        Menu menu = tvEx.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        View profile_view = findViewById(R.id.ic_profile);
        View matches_view = findViewById(R.id.ic_matched);
        if(firstStart){
            showToolTip_profile(new ViewPosition(profile_view));
        }
        SharedPreferences newPref = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = newPref.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }

    private void showToolTip_profile(ShowCasePosition position) {
        new ShowCaseView.Builder(MainActivity.this).withTypedPosition(position).withTypedRadius(new Radius(186F)).withContent("First time upload your profile picture and click on Confirm, otherwise your app will not be working fine").build().show(MainActivity.this);
    }

    public void DisLikeBtn(View view) {
    }
}