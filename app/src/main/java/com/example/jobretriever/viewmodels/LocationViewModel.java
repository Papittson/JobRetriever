package com.example.jobretriever.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Location;
import com.example.jobretriever.repositories.LocationRepository;
import com.google.firebase.firestore.DocumentSnapshot;

public class LocationViewModel extends ViewModel {
    private static LocationViewModel instance;
    private final MutableLiveData<Location> location = new MutableLiveData<>();
    private final MutableLiveData<Integer> errorMessage = new MutableLiveData<>();
    private LocationViewModel() {
    }

    public static LocationViewModel getInstance() {
        if (instance == null) {
            instance = new LocationViewModel();
        }
        return instance;
    }

    public void getLocationById(String id) {
        LocationRepository.getInstance().getById(id).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot doc = task.getResult();
                    Location obj = doc.toObject(Location.class);
                    obj.setId(doc.getId());
                    this.location.postValue(obj);

            } else {
                errorMessage.postValue(R.string.error_loading_location);
                if(task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
    }

    public MutableLiveData<Integer> getError() {
        return errorMessage;
    }
    public MutableLiveData<Location> getLocation() {
        return location;
    }

}
