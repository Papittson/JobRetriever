package com.example.jobretriever.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jobretriever.models.User;
import com.example.jobretriever.repositories.UserRepository;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();
    private final MutableLiveData<User> user = new MutableLiveData<>();
    private static UserViewModel instance;

    private UserViewModel() {}

    public static UserViewModel getInstance() {
        if(instance == null) {
            instance = new UserViewModel();
        }
        return instance;
    }

    public void getAll() {
        UserRepository.getInstance().getAll().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<User> users = new ArrayList<>();
                for(QueryDocumentSnapshot doc : task.getResult()) {
                    User obj = doc.toObject(User.class);
                    obj.setId(doc.getId());
                    users.add(obj);
                }
                this.users.postValue(users);
            } else {
                errorMessage.postValue("Error loading users");
                if(task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
    }

    public MutableLiveData<List<User>> getUsers() {
        return users;
    }

    public void signIn(String email, String password) {
        UserRepository.getInstance().getUserByCredentials(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean isUserPosted = false;
                for(QueryDocumentSnapshot doc : task.getResult()) {
                    User obj = doc.toObject(User.class);
                    obj.setId(doc.getId());
                    user.postValue(obj);
                    isUserPosted = true;
                    break;
                }
                if (!isUserPosted) {
                    errorMessage.postValue("Error wrong mail or password");
                }
            } else {
                errorMessage.postValue("Error loading users");
                if(task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
    }

    public void signUp(User user) {
        UserRepository.getInstance().getUserByMail(user.getMail()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    UserRepository.getInstance().add(user).addOnCompleteListener(task2 -> {
                        if (task2.isSuccessful()) {
                            user.setId(task2.getResult().getId());
                            this.user.postValue(user);
                        } else {
                            errorMessage.postValue("Error during sign up please try again");
                            if(task2.getException() != null) {
                                task2.getException().printStackTrace();
                            }
                        }
                    });
                } else {
                    errorMessage.postValue("Error this mail is already used");
                }
            } else {
                errorMessage.postValue("Error checking if mail exists");
                if(task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
    }

    public MutableLiveData<String> getError() {
        return errorMessage;
    }

    public boolean isLoggedIn() {
        return user.getValue() != null;
    }

    public MutableLiveData<User> getUser() {
        return user;
    }
}
