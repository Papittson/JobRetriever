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
    private static final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private static MutableLiveData<List<User>> users = new MutableLiveData<>();
    private static MutableLiveData<User> user =new MutableLiveData<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static MutableLiveData<List<User>> getUsers() {
        if (users == null) {
            users = new MutableLiveData<>();
        }
        UserRepository.getInstance().getAll().addOnCompleteListener(task -> {
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
    public MutableLiveData<User> signIn(String mail, String password) {
        if (user == null) {
            user = new MutableLiveData<>();
        }
        UserRepository.getInstance().getUserByCredentials(mail, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> usersList = new ArrayList<>();
                task.getResult().forEach(doc -> {
                    User obj = doc.toObject(User.class);
                    obj.setId(doc.getId());
                    usersList.add(obj);
                });
                if (usersList.size() != 0) {
                    user.postValue(usersList.get(0));

                } else {
                    errorMessage.postValue("Error wrong mail or password");

                }

            } else {
                errorMessage.postValue("Error loading users");
                Objects.requireNonNull(task.getException()).printStackTrace();
            }
        });
        return user;
    }

    public MutableLiveData<User> signUp(User newUser) {
        UserRepository.getInstance().getUserByMail(newUser.getMail()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    UserRepository.getInstance().add(newUser).addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            newUser.setId(task2.getResult().getId());
                            user.postValue(newUser);
                        } else {
                            if(task2.getException() != null) {
                                task2.getException().printStackTrace();
                            }
                            errorMessage.postValue("Error during sign up please try again");
                        }
                    });
                } else {
                    errorMessage.postValue("Error this mail is already used");
                }
            } else {
                if(task.getException() != null) {
                    task.getException().printStackTrace();
                }
                errorMessage.postValue("Error checking if mail exists");
            }
        });
        return user;
    }

    public static MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public static boolean isLoggedIn() {
        return user.getValue() != null;
    }

    public static MutableLiveData<User> getUser() {
        return user;
    }



}
