package com.example.tintooth;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.tintooth.Matches.MatchesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class TopNavigationView {
     private static final String TAG = "TopNavigationHelper";
     public static void setupTopNavigationView(BottomNavigationViewEx tv){
        Log.d(TAG, "setupTopNavigation: setting up navigationview");
    }

    public static void enableNavigation(final Context context, BottomNavigationViewEx view){
         view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
             @Override
             public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                 switch (item.getItemId()){
                     case R.id.ic_profile:
                         Intent i = new Intent(context, SettingsActivity.class);
                         context.startActivity(i);
                         break;
                     case R.id.ic_matched:
                         Intent intent =new Intent(context, MatchesActivity.class);
                         context.startActivity(intent);
                         break;

                 }
                 return false;
             }
         });
    }
}
