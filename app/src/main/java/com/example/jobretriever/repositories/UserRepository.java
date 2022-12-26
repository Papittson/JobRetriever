package com.example.jobretriever.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository extends JRRepository {

    public static UserRepository getInstance() {
        return (UserRepository) getInstance("users");
    }

    public Task<QuerySnapshot> getUserByCredentials(String mail,String password){
        return collection.whereEqualTo("mail",mail).whereEqualTo("password",password).get();
    }

    public Task<QuerySnapshot> getUserByMail(String mail){
        return collection.whereEqualTo("mail",mail).get();
    }

}
