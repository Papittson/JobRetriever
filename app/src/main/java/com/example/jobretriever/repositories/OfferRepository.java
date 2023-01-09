package com.example.jobretriever.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class OfferRepository extends JRRepository {
    private static OfferRepository instance;

    OfferRepository() {
        super("offers");
    }

    public static OfferRepository getInstance() {
        if(instance == null)
            instance = new OfferRepository();
        return instance;
    }

    public Task<QuerySnapshot> getFavorites(List<String> favoritesId) {
        return collection.whereIn("id", favoritesId).get();
    }

    public Task<QuerySnapshot> getAvailables(String userId) {
        return collection.whereEqualTo("employerId", userId).get();
    }

    public Task<QuerySnapshot> getApplieds(List<String> applicationsId) {
        return collection.whereIn("id", applicationsId).get();
    }
}
