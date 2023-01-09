package com.example.jobretriever.repositories;

import com.example.jobretriever.enums.DurationType;
import com.example.jobretriever.models.Entity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public abstract class JRRepository {
    FirebaseFirestore db;
    FirebaseStorage storage;
    CollectionReference collection;

    JRRepository(String collectionName) {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        collection = db.collection(collectionName);
    }

    public Task<QuerySnapshot> search(String city, DurationType durationType) {
        Query query = collection;
        if(city != null) {
            query = query.whereEqualTo("location", city);
        }
        if(durationType != null) {
            query = query.whereEqualTo("duration", durationType);
        }
        return query.get();
    }

    public Task<QuerySnapshot> getAll(long limit) {
        return collection.orderBy("createdAt").limit(limit).get();
    }

    public Task<DocumentSnapshot> getById(String id) {
        return collection.document(id).get();
    }

    public Task<Void> add(Entity entity) {
        return collection.document(entity.getId()).set(entity);
    }

    public Task<Void> update(String id, String field, Object value, Object... moreFieldsAndValues) {
        return collection.document(id).update(field, value, moreFieldsAndValues);
    }

}
