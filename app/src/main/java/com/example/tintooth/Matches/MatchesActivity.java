package com.example.tintooth.Matches;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.service.autofill.FieldClassification;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageButton;

import com.example.tintooth.Cards.cards;
import com.example.tintooth.MainActivity;
import com.example.tintooth.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MatchesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mMatchesAdapter;
    private RecyclerView.LayoutManager mMatchesLayoutManager;
    private ImageButton mBack;
    private DatabaseReference current;
    private ValueEventListener listen;
    private HashMap<String, Integer> mList = new HashMap<>();
    private String currentUserId, mLastTimeStamp, mLastMessage, lastSeen;
    DatabaseReference mCurrUserIdInsideMatchConnections, mCheckLastSeen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matches);
        mBack = findViewById(R.id.matchesBack);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MatchesActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mMatchesLayoutManager = new LinearLayoutManager(MatchesActivity.this);
        mRecyclerView.setLayoutManager(mMatchesLayoutManager);
        mMatchesAdapter = new MatchesAdapter(getDataSetMatches(), MatchesActivity.this);
        mRecyclerView.setAdapter(mMatchesAdapter);
        getUserMatchId();
    }

    private void getUserMatchId() {
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches");
        matchDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot match: snapshot.getChildren()){
                        FetchMatchInformation(match.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void FetchMatchInformation(String key) {
        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String userId = snapshot.getKey();
                    String name = "";
                    String profileImageUrl = "";
                    if(snapshot.child("name").getValue()!=null){
                        name = snapshot.child("name").getValue().toString();
                    }
                    if(snapshot.child("profileImageUrl").getValue()!=null){
                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
                    }
                    MatchesObject obj = new MatchesObject(userId, name, profileImageUrl);
                    resultsMatches.add(obj);
                    mMatchesAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    private void getLastMessageInfo(DatabaseReference userDb){
        mCurrUserIdInsideMatchConnections = userDb.child("connections").child(currentUserId);
        mCurrUserIdInsideMatchConnections.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("lastMessage").getValue()!= null && snapshot.child("lastTimeStamp").getValue()!=null && snapshot.child("lastSend").getValue()!=null){
                        mLastMessage = snapshot.child("lastMessage").getValue().toString();
                        mLastTimeStamp = snapshot.child("lastTimeStamp").getValue().toString();
                        lastSeen = snapshot.child("lastSend").getValue().toString();
                    }else{
                        mLastMessage = "No message";
                        mLastTimeStamp = " ";
                        lastSeen = "true";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
//    private void getUserMatchId(){
//        Query sortedMatchesMyLastTimeStamp = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId).child("connections").child("matches").orderByChild("lastTimeStamp");
//        sortedMatchesMyLastTimeStamp.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    for(DataSnapshot match: snapshot.getChildren()){
//                        FetchMatchInformation(match.getKey(), match.child("Chatid").toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

//    private void FetchMatchInformation(final String key, final String chatid) {
//        DatabaseReference userDb = FirebaseDatabase.getInstance().getReference().child("Users").child(key);
//        getLastMessageInfo(userDb);
//        userDb.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists()){
//                    String userId = snapshot.getKey();
//                    String name = "";
//                    String profileImageUrl = "";
//                    String need = "";
//                    String give = "";
//                    String budget = "";
//                    final String lastMessage = "";
//                    String lastTimeStamp = "";
//                    if(snapshot.child("name").getValue()!=null){
//                        name = snapshot.child("name").getValue().toString();
//                    }
//                    if(snapshot.child("profileImageUrl").getValue()!=null){
//                        profileImageUrl = snapshot.child("profileImageUrl").getValue().toString();
//                    }
//                    if(snapshot.child("need").getValue()!=null){
//                        need = snapshot.child("need").getValue().toString();
//                    }
//                    if(snapshot.child("give").getValue()!=null){
//                        give = snapshot.child("give").getValue().toString();
//                    }
//                    if(snapshot.child("budget").getValue().toString()!=null){
//                        budget = snapshot.child("budget").getValue().toString();
//                    }
//                    String milliSec = mLastTimeStamp;
//                    Long now;
//                    try{
//                        now = Long.parseLong(milliSec);
//                        lastTimeStamp = convertMilliToRelative(now);
//                        String[] arrOfStr = lastTimeStamp.split(",");
//                        mLastTimeStamp = arrOfStr[0];
//                    }catch (Exception e){
//
//                    }
//                    MatchesObject obj = new MatchesObject(userId, name, profileImageUrl, need, give, budget, mLastMessage, mLastTimeStamp, chatid, lastMessage);
//                    if(mList.containsKey(chatid)){
//                        int key = mList.get(chatid);
//                        resultsMatches.set(resultsMatches.size() - key, obj);
//                    }else{
//                        resultsMatches.add(0, obj);
//                        mList.put(chatid, resultsMatches.size());
//                    }
//                    mMatchesAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private String convertMilliToRelative(Long now) {
        String time = DateUtils.getRelativeDateTimeString(this, now, DateUtils.SECOND_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL).toString();
        return time;
    }

    private ArrayList<MatchesObject> resultsMatches = new ArrayList<>();
    private List<MatchesObject> getDataSetMatches(){
        return resultsMatches;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        return;
    }
}