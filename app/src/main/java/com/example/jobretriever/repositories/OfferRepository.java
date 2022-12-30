package com.example.jobretriever.repositories;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class OfferRepository extends JRRepository {
    private static OfferRepository instance;

    OfferRepository(){
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        collection = db.collection("offers");
    }

    public static OfferRepository getInstance() {
        if(instance == null)
            instance = new OfferRepository();
        return instance;
    }


}
