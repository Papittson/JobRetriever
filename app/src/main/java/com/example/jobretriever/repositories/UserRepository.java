package com.example.jobretriever.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository extends JRRepository {
    private static UserRepository instance;

    UserRepository() {
        super("users");
    }

    public static UserRepository getInstance() {
        if(instance == null)
            instance = new UserRepository();
        return instance;
    }

    public Task<QuerySnapshot> getUserByCredentials(String mail,String password){
        return collection.whereEqualTo("mail",mail).whereEqualTo("password",password).get();
    }

    public Task<QuerySnapshot> getUserByMail(String mail) {
        return collection.whereEqualTo("mail",mail).get();
    }
}
