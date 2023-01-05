package com.example.jobretriever.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class LocationRepository extends JRRepository {
    private static LocationRepository instance;

    LocationRepository(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        collection = db.collection("cities");
    }

    public static LocationRepository getInstance() {
        if(instance == null)
            instance = new LocationRepository();
        return instance;
    }
}