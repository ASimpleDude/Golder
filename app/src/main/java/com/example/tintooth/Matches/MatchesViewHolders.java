package com.example.tintooth.Matches;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tintooth.Chat.ChatActivity;
import com.example.tintooth.R;

public class MatchesViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView mMatchId, mMatchName, mLastTimeStamp, mLastMessage;
    public ImageView mNotificationDot;
    public ImageView mMatchImage;

    public MatchesViewHolders(@NonNull View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mMatchId = itemView.findViewById(R.id.Matchid);
        mMatchName = itemView.findViewById(R.id.MatchName);
        mLastMessage = itemView.findViewById(R.id.lastMessage);
        mLastTimeStamp = itemView.findViewById(R.id.lastTimeStamp);
        mMatchImage = itemView.findViewById(R.id.MatchImage);
        mNotificationDot = itemView.findViewById(R.id.notification_dot);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(v.getContext(), ChatActivity.class);
        Bundle b = new Bundle();
        b.putString("matchId", mMatchId.getText().toString());
        b.putString("matchName", mMatchName.getText().toString());
        b.putString("lastMessage", mLastMessage.getText().toString());
        b.putString("lastTimeStamp", mLastTimeStamp.getText().toString());
        intent.putExtras(b);
        v.getContext().startActivity(intent);
    }
}
