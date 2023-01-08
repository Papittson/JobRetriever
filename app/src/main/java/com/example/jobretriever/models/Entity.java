package com.example.jobretriever.models;

import com.google.firebase.firestore.Exclude;

@SuppressWarnings("unused")
public class Entity {
    @Exclude
    String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
}
