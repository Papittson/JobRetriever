package com.example.jobretriever.repositories;

import com.example.jobretriever.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

public class UserRepository {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final CollectionReference usersCollection = db.collection("users");


    public static Task<QuerySnapshot> getUsers(){
        return usersCollection.get();
    }


    public static Task<QuerySnapshot> getUserByCredentials(String mail,String password){
        return usersCollection.whereEqualTo("mail",mail).whereEqualTo("password",password).get();
    }

    public static Task<QuerySnapshot> getUserByMail(String mail){
        return usersCollection.whereEqualTo("mail",mail).get();
    }

  public static Task<Void> setNewUser(User user){
        return usersCollection.document().set(user);
  }

}
