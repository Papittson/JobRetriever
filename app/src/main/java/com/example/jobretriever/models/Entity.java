package com.example.jobretriever.models;

import static com.example.jobretriever.activities.MainActivity.DATE_FORMATTER;

import com.google.firebase.firestore.IgnoreExtraProperties;

import java.time.LocalDate;
import java.util.UUID;

@SuppressWarnings("unused")
@IgnoreExtraProperties
public abstract class Entity {
    String createdAt;
    String id;

    public Entity() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDate.now().format(DATE_FORMATTER);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
