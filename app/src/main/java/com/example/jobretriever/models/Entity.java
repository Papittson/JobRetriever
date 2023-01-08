package com.example.jobretriever.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

@SuppressWarnings("unused")
@IgnoreExtraProperties
public class Entity {
    @Exclude
    String id;

    @Exclude
    public String getId() {
        return id;
    }
    @Exclude
    public void setId(String id) {
        this.id = id;
    }
}
