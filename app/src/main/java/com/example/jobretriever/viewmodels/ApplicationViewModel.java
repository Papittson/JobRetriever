package com.example.jobretriever.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Application;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.repositories.ApplicationRepository;

import java.util.List;

public class ApplicationViewModel extends ViewModel {
    private final MutableLiveData<Integer> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<List<Application>> applications = new MutableLiveData<>();
    private final MutableLiveData<Application> application = new MutableLiveData<>();
    private static ApplicationViewModel instance;

    private ApplicationViewModel() {
    }

    public static ApplicationViewModel getInstance() {
        if (instance == null) {
            instance = new ApplicationViewModel();
        }
        return instance;
    }

    public void addApplication(Application application) {
        ApplicationRepository.getInstance().add(application).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                application.setId(task.getResult().getId());
                this.application.postValue(application);
            } else {
                errorMessage.postValue(R.string.error_loading_offers); // TODO Changer message
                if (task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
    }

    public MutableLiveData<List<Application>> getApplications() {
        return applications;
    }

    public MutableLiveData<Application> getApplication() {
        return application;
    }

    public MutableLiveData<Integer> getError() {
        return errorMessage;
    }
}
