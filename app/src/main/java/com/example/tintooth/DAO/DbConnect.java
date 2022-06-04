package com.example.tintooth.DAO;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DbConnect {
    public static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static final DatabaseReference myRef = database.getReference();

}
