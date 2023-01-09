package com.example.jobretriever.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.util.UUID;

@SuppressWarnings("unused")
@IgnoreExtraProperties
public abstract class Entity {
    Timestamp createdAt;
    String id;

    public Entity() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = Timestamp.now();
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
