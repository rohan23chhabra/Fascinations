package com.example.fascinations.db;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DB {
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance()
            .getReference();

    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageReference = storage.getReference();

    public static DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public static StorageReference getStorageReference() {
        return storageReference;
    }
}
