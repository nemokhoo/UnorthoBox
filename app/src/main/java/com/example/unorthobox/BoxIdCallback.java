package com.example.unorthobox;

import com.google.firebase.database.DatabaseError;

public interface BoxIdCallback {
    void onBoxIdFound(String boxId);
    void onBoxIdNotFound();
    void onError(DatabaseError databaseError);
}
