package com.example.jobretriever.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class JRRepository {
    FirebaseFirestore db;
    FirebaseStorage storage;
    CollectionReference collection;

    JRRepository(String collectionName) {
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        collection = db.collection(collectionName);
    }

    public Task<QuerySnapshot> getAll() {
        return collection.get();
    }

    public Task<DocumentSnapshot> getById(String id) {
        return collection.document(id).get();
    }

    public Task<DocumentReference> add(Object data) {
        return collection.add(data);
    }

    public Task<Void> update(String id, String field, Object value, Object... moreFieldsAndValues) {
        return collection.document(id).update(field, value, moreFieldsAndValues);
    }

    public Task<Void> delete(String id) {
        return collection.document(id).delete();
    }
}
