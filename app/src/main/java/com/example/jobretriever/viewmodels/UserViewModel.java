package com.example.jobretriever.viewmodels;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jobretriever.models.User;
import com.example.jobretriever.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserViewModel extends ViewModel {
    private static MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private static MutableLiveData<List<User>> users;
    private static final MutableLiveData<Boolean> loggedIn = new MutableLiveData<>();
    private static MutableLiveData<User> user;


    @RequiresApi(api = Build.VERSION_CODES.N)
    public MutableLiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<>();
        }
        UserRepository.getUsers().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> usersList = new ArrayList<>();
                task.getResult().forEach(doc -> {
                    User obj = doc.toObject(User.class);
                    obj.setId(doc.getId());
                    usersList.add(obj);
                });
                users.postValue(usersList);
            } else {
                errorMessage.postValue("Error loading users");
                Objects.requireNonNull(task.getException()).printStackTrace();
            }
        });
        return users;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public MutableLiveData<Boolean> signIn(String mail, String password) {
        if (user == null) {
            user = new MutableLiveData<>();
        }
        UserRepository.getUserByCredentials(mail, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> usersList = new ArrayList<>();
                task.getResult().forEach(doc -> {
                    User obj = doc.toObject(User.class);
                    obj.setId(doc.getId());
                    usersList.add(obj);
                });
                if (usersList.size() != 0) {
                    user.postValue(usersList.get(0));
                    loggedIn.postValue(true);
                } else {
                    errorMessage.postValue("Error wrong mail or password");
                    loggedIn.postValue(false);
                }

            } else {
                errorMessage.postValue("Error loading users");
                Objects.requireNonNull(task.getException()).printStackTrace();
            }
        });
        return loggedIn;
    }

    public MutableLiveData<Boolean> signUp(User user) {
        UserRepository.getUserByMail(user.getMail()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    UserRepository.setNewUser(user).addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            loggedIn.postValue(true);
                        } else {
                            loggedIn.postValue(false);
                            errorMessage.postValue("Error during sign up please try again");
                        }
                    });
                } else {
                    errorMessage.postValue("Error this mail is already used");
                }
            } else {
                errorMessage.postValue("Error checking if mail exists");
            }
        });
        return loggedIn;
    }

    public static MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }



    public static MutableLiveData<Boolean> getLoggedIn() {
        return loggedIn;
    }

    public static MutableLiveData<User> getUser() {
        return user;
    }


}
