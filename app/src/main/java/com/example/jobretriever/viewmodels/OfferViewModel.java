package com.example.jobretriever.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Location;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.models.User;
import com.example.jobretriever.repositories.LocationRepository;
import com.example.jobretriever.repositories.OfferRepository;
import com.example.jobretriever.repositories.UserRepository;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OfferViewModel extends ViewModel {
    private static OfferViewModel instance;
    private final MutableLiveData<Integer> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Offer> offer = new MutableLiveData<>();
    private final MutableLiveData<List<Offer>> offers = new MutableLiveData<>();

    private OfferViewModel() {
    }

    public static OfferViewModel getInstance() {
        if (instance == null) {
            instance = new OfferViewModel();
        }
        return instance;
    }

    public void getAll(String searchQuery) {
        OfferRepository.getInstance().getAll().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Offer> list = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Offer obj = doc.toObject(Offer.class);
                    obj.setId(doc.getId());
                    if (searchQuery == null || obj.getTitle().toLowerCase().contains(searchQuery.toLowerCase())) {
                        list.add(obj);
                        UserRepository.getInstance().getById(obj.getEmployerID()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                obj.setEmployer(task1.getResult().toObject(User.class));
                                // TODO C'est quoi cette valeur d'ID en dur ?
                                LocationRepository.getInstance().getById("8RL9flb7zvNTddfUcPi9").addOnCompleteListener(task2 -> {
                                    if (task2.isSuccessful()) {
                                        Location location = task2.getResult().toObject(Location.class);
                                        obj.setLocation(location);
                                        offers.postValue(list);
                                    } else {
                                        errorMessage.postValue(R.string.error_loading_location);
                                        if (task.getException() != null) {
                                            task.getException().printStackTrace();
                                        }
                                    }
                                });
                            } else {
                                errorMessage.postValue(R.string.error_loading_users);
                                if (task.getException() != null) {
                                    task.getException().printStackTrace();
                                }
                            }
                        });


                    }
                }
                if (list.isEmpty()) {
                    offers.postValue(list);
                }
            } else {
                errorMessage.postValue(R.string.error_loading_offers);
                if (task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });

    }

    public MutableLiveData<List<Offer>> getOffers() {
        return offers;
    }

    public MutableLiveData<Integer> getError() {
        return errorMessage;
    }
}
