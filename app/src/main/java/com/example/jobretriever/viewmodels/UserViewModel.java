package com.example.jobretriever.viewmodels;

import static com.example.jobretriever.enums.UserType.APPLICANT;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jobretriever.R;
import com.example.jobretriever.enums.UserType;
import com.example.jobretriever.models.Applicant;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.models.User;
import com.example.jobretriever.repositories.UserRepository;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserViewModel extends ViewModel {
    private static UserViewModel instance;
    private final MutableLiveData<Integer> errorMessage = new MutableLiveData<>(null);
    private final MutableLiveData<User> selectedUser = new MutableLiveData<>(null);
    private final MutableLiveData<User> authUser = new MutableLiveData<>(null);

    private UserViewModel() {
    }

    public static UserViewModel getInstance() {
        if (instance == null) {
            instance = new UserViewModel();
        }
        return instance;
    }

    public void removeFavorite(String offerId) {
        User user = this.authUser.getValue();
        if (!(user instanceof Applicant)) {
            errorMessage.postValue(R.string.error_loading_users); // TODO Changer message
            return;
        }
        List<String> offersId = new ArrayList<>(((Applicant) user).getFavoritesId());
        offersId.remove(offerId);
        UserRepository.getInstance().update(user.getId(), "favoritesId", offersId)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ((Applicant) user).setFavoritesId(offersId);
                    } else {
                        errorMessage.postValue(R.string.error_loading_users);
                    }
                });
    }

    public void addFavorite(String offerId) {
        User user = this.authUser.getValue();
        if (!(user instanceof Applicant)) {
            errorMessage.postValue(R.string.error_loading_users); // TODO Changer message
            return;
        }
        List<String> offersId = new ArrayList<>(((Applicant) user).getFavoritesId());
        offersId.add(offerId);
        UserRepository.getInstance().update(user.getId(), "favoritesId", offersId)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ((Applicant) user).setFavoritesId(offersId);
                    } else {
                        errorMessage.postValue(R.string.error_loading_users);
                    }
                });
    }

    public void signIn(String email, String password) {
        UserRepository.getInstance().getUserByCredentials(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(task.getResult().isEmpty()) {
                    errorMessage.postValue(R.string.error_wrong_credentials);
                    return;
                }
                User user = null;
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    if(doc.get("userType", UserType.class) == APPLICANT) {
                        user = doc.toObject(Applicant.class);
                    } else {
                        user = doc.toObject(Employer.class);
                    }
                    break;
                }
                this.authUser.postValue(user);
            } else {
                errorMessage.postValue(R.string.error_loading_users);
            }
        });
    }

    public void signUp(User user) {
        UserRepository.getInstance().getUserByMail(user.getMail()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    UserRepository.getInstance().add(user).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            this.authUser.postValue(user);
                        } else {
                            errorMessage.postValue(R.string.error_during_sign_up);
                        }
                    });
                } else {
                    errorMessage.postValue(R.string.error_mail_already_used);
                }
            } else {
                errorMessage.postValue(R.string.error_checking_mail);
            }
        });
    }

    public void disconnectUser() {
        authUser.postValue(null);
    }

    public boolean isLoggedIn() {
        return authUser.getValue() != null;
    }

    public MutableLiveData<User> getAuthUser() {
        return authUser;
    }

    public MutableLiveData<User> getSelectedUser() {
        return selectedUser;
    }

    public MutableLiveData<Integer> getError() {
        return errorMessage;
    }
}
