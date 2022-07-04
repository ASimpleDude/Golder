package com.example.tintooth.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tintooth.Matches.MatchesActivity;
import com.example.tintooth.R;
import com.example.tintooth.SendNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mChatAdapter;
    private RecyclerView.LayoutManager mChatLayoutManager;

    private EditText mSendText;
    private ImageButton mBack;
    private ImageButton mSendButton;

    private String notification;
    private String currentUserID, matchID, chatID;
    private String matchName, matchGive, matchNeed, matchBudget, matchProfile;
    private String lastMessage, lastTimeStamp;
    private String message, createdByUser, isSeen, messageId, currentUserName;
    private Boolean currentUserBoolean;

    ValueEventListener seenListener;
    DatabaseReference mDatabaseUser, mDatabaseChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        matchID = getIntent().getExtras().getString("matchId");
        matchName = getIntent().getExtras().getString("matchName");
        matchGive = getIntent().getExtras().getString("give");
        matchNeed = getIntent().getExtras().getString("need");
        matchBudget = getIntent().getExtras().getString("budget");
        matchProfile = getIntent().getExtras().getString("profile");
        currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("connections").child("matches").child(matchID).child("ChatId");

        mDatabaseChat = FirebaseDatabase.getInstance().getReference().child("Chat");

        getChatId();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setFocusable(false);
        mChatLayoutManager = new LinearLayoutManager(ChatActivity.this);
        mRecyclerView.setLayoutManager(mChatLayoutManager);
        mChatAdapter = new ChatAdapter(getDataSetChat(), ChatActivity.this);
        mRecyclerView.setAdapter(mChatAdapter);

        mSendText = findViewById(R.id.message);
        mBack = findViewById(R.id.chatBack);

        mSendButton = findViewById(R.id.send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(bottom< oldBottom){
                    mRecyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                        }
                    }, 100);
                }
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ChatActivity.this, MatchesActivity.class);
                startActivity(i);
                finish();
                return;
            }
        });

        Toolbar toolbar = findViewById(R.id.chatToolbar);
        setSupportActionBar(toolbar);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        Map onChat = new HashMap();
        onChat.put("onChat", matchID);
        ref.updateChildren(onChat);

        DatabaseReference current = FirebaseDatabase.getInstance().getReference("Users")
                .child(matchID).child("connections").child("matches").child(currentUserID);

        Map lastSeen = new HashMap();
        lastSeen.put("lastSeen", "false");
        current.updateChildren(lastSeen);
    }

    @Override
    protected void onPause() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        Map onchat = new HashMap();
        onchat.put("onChat", "None");
        ref.updateChildren(onchat);
        super.onPause();
    }

    @Override
    protected void onStop() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        Map onchat = new HashMap();
        onchat.put("onChat", "None");
        ref.updateChildren(onchat);
        super.onStop();
    }

    private void seenMessage(final String text) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(matchID);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.child("onChat").exists()){
                        if (snapshot.child("notificationKey").exists()) {
                            notification = snapshot.child("notificationKey").getValue().toString();
                        }
                        else{
                            notification = "";
                        }
                        if(!snapshot.child("onChat").getValue().toString().equals(currentUserID)){
                            new SendNotification(text, "New Message from: "+currentUserName, notification,
                                    "activityToBeOpened", "matchesActivity");
                        }else{
                            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(currentUserID).child("connections").child("matches").child(matchID);
                            Map seenInfo = new HashMap();
                            seenInfo.put("lastSend", "false");
                            ref.updateChildren(seenInfo);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_menu,menu);
        TextView mMatchNameTextView = findViewById(R.id.chatToolbar);
        mMatchNameTextView.setText(matchName);
        return true;
    }

    public void showProfile(View v){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.item_profile,null);

        TextView name = (TextView) popupView.findViewById(R.id.name);
        ImageView image = (ImageView) popupView.findViewById(R.id.image);
        TextView budget =  (TextView) popupView.findViewById(R.id.budget);
        ImageView mNeedImage = (ImageView) popupView.findViewById(R.id.needImage);
        ImageView mGiveImage = (ImageView) popupView.findViewById(R.id.giveImage);

        name.setText(matchName);
        budget.setText(matchBudget);

        if(matchNeed.equals("Netflix")){
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.netflix));
        }else if(matchNeed.equals("Amazon Prime")){
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.amazon));
        }else if(matchNeed.equals("Hulu")){
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hulu));
        }else if(matchNeed.equals("Vudu")){
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vudu));
        }else if(matchNeed.equals("HBO Now")){
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hbo));
        }else if(matchNeed.equals("Youtube Originals")){
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.youtube));
        }else{
            mNeedImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.none));
        }

        if(matchNeed.equals("Netflix")){
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.netflix));
        }else if(matchNeed.equals("Amazon Prime")){
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.amazon));
        }else if(matchNeed.equals("Hulu")){
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hulu));
        }else if(matchNeed.equals("Vudu")){
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.vudu));
        }else if(matchNeed.equals("HBO Now")){
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.hbo));
        }else if(matchNeed.equals("Youtube Originals")){
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.youtube));
        }else{
            mGiveImage.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.none));
        }

        switch (matchProfile){
            case "default":
                Glide.with(popupView.getContext()).load(R.drawable.profile).into(image);
                break;
            default:
                Glide.clear(image);
                Glide.with(popupView.getContext()).load(matchProfile).into(image);
                break;
        }

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        hideSoftKeyBoard();

        popupWindow.showAtLocation(v, Gravity.CENTER, 0 ,0);
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });

    }

    private void hideSoftKeyBoard() {
        InputMethodManager imm= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if(imm.isAcceptingText()){
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.unmacth){
            new AlertDialog.Builder(ChatActivity.this)
                    .setTitle("Unmatch")
                    .setMessage("Want to unmatch?")
                    .setPositiveButton("Unmatch", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteMatch(matchID);
                            Intent i = new Intent(ChatActivity.this, MatchesActivity.class);
                            startActivity(i);
                            finish();
                            Toast.makeText(ChatActivity.this, "Unmatched successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Dismiss",null)
                    .setIcon(R.drawable.ic_alert).show();

        }
        else if(item.getItemId() == R.id.viewProfile){
            showProfile(findViewById(R.id.cardView));
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteMatch(String matchID) {
        DatabaseReference matchId_in_UserId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("connections").child("matches").child(matchID);
        DatabaseReference userId_in_matchId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(matchID).child("connections").child("matches").child(currentUserID);
        DatabaseReference yeps_in_matchId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(matchID).child("connections").child("yeps").child(currentUserID);
        DatabaseReference yeps_in_UserId_dbReference = FirebaseDatabase.getInstance().getReference().child("Users")
                .child(currentUserID).child("connections").child("yeps").child(matchID);
        DatabaseReference matchId_chat_dbReference = FirebaseDatabase.getInstance().getReference().child("Chat")
                .child(chatID);
        matchId_chat_dbReference.removeValue();
        matchId_in_UserId_dbReference.removeValue();
        userId_in_matchId_dbReference.removeValue();
        yeps_in_matchId_dbReference.removeValue();
        yeps_in_UserId_dbReference.removeValue();
    }
    private void sendMessage(){
        final String sendMessageText = mSendText.getText().toString();
        long now = System.currentTimeMillis();
        String timeStamp = Long.toString(now);
        if(!sendMessageText.isEmpty()){
            DatabaseReference newMessageDb = mDatabaseChat.push();
            Map newMessage = new HashMap();
            newMessage.put("createdByUser", currentUserID);
            newMessage.put("text", sendMessageText);
            newMessage.put("timeStamp", timeStamp);
            newMessage.put("seen", false);
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        if(snapshot.child("name").exists()){
                            currentUserName = snapshot.child("name").getValue().toString();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            lastMessage = sendMessageText;
            lastTimeStamp = timeStamp;
            updateLastMessage();
            seenMessage(sendMessageText);
            newMessageDb.setValue(newMessage);
        }
        mSendText.setText(null);
    }

    private void updateLastMessage() {
        DatabaseReference currUserDb = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID).child("connections").child("matches").child(matchID);
        DatabaseReference matchDb = FirebaseDatabase.getInstance().getReference().child("Users").child(matchID).child("connections").child("matches").child(currentUserID);
        Map lastMessageMap = new HashMap();
        lastMessageMap.put("lastMessage", lastMessage);
        Map lastTimestampMap = new HashMap();
        lastTimestampMap.put("lastTimeStamp", lastTimeStamp);
        Map lastSeen = new HashMap();
        lastSeen.put("lastSeen", "true");
        currUserDb.updateChildren(lastSeen);
        currUserDb.updateChildren(lastMessageMap);
        currUserDb.updateChildren(lastTimestampMap);
        matchDb.updateChildren(lastMessageMap);
        matchDb.updateChildren(lastTimestampMap);
    }

    private void getChatId() {
        mDatabaseUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    chatID = snapshot.getValue().toString();
                    mDatabaseChat = mDatabaseChat.child(chatID);
                    getChatMessage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getChatMessage(){
        mDatabaseChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    messageId = null;
                    message = null;
                    createdByUser = null;
                    isSeen = null;
                    if(snapshot.child("text").getValue()!=null){
                        message = snapshot.child("text").getValue().toString();
                    }
                    if(snapshot.child("createdByUser").getValue()!=null){
                        createdByUser = snapshot.child("createdByUser").getValue().toString();
                    }
                    if(snapshot.child("seen").getValue()!=null){
                        isSeen = snapshot.child("seen").getValue().toString();
                    }else{
                        isSeen = "true";
                    }
                    messageId = snapshot.getKey().toString();
                    if(messageId!= null && createdByUser!=null){
                        currentUserBoolean = false;
                        if(createdByUser.equals(currentUserID)){
                            currentUserBoolean = true;
                        }
                        ChatObject newMessage = null;
                        if(isSeen.equals("false")){
                            if(!currentUserBoolean){
                                isSeen = "true";
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Chat").child(chatID).child(messageId);
                                Map seenInfo = new HashMap();
                                seenInfo.put("seen", "true");
                                reference.updateChildren(seenInfo);
                                newMessage = new ChatObject(message, currentUserBoolean, true);
                            }else{
                                newMessage = new ChatObject(message, currentUserBoolean, false);
                            }
                        }else{
                            newMessage = new ChatObject(message, currentUserBoolean, true);
                        }
                        DatabaseReference usersInChat = FirebaseDatabase.getInstance().getReference().child("Chat").child(matchID);
                        resultsChat.add(newMessage);
                        mChatAdapter.notifyDataSetChanged();
                        if(mRecyclerView.getAdapter() != null && resultsChat.size() > 0){
                            mRecyclerView.smoothScrollToPosition(resultsChat.size() - 1);
                        }else{
                            Toast.makeText(ChatActivity.this, "Chat empty", Toast.LENGTH_SHORT).show();
                        }
                    }
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
    }

    private ArrayList<ChatObject> resultsChat = new ArrayList<>();

    private List<ChatObject> getDataSetChat(){
        return resultsChat;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        return;
    }
}