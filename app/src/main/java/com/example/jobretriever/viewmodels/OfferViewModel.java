package com.example.jobretriever.viewmodels;

import static com.example.jobretriever.models.ApplicationStatus.PENDING;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jobretriever.R;
import com.example.jobretriever.models.DurationType;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.models.Offer;
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

    public void getAll() {
        getAll(null, null, null);
    }

    public void getAll(String searchQuery, String city, DurationType durationType) {
        OfferRepository.getInstance().getAll().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Offer> list = new ArrayList<>();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    Offer obj = doc.toObject(Offer.class);
                    obj.setId(doc.getId());
                    boolean addToList = searchQuery == null || obj.getTitle().toLowerCase().contains(searchQuery.toLowerCase());

                    if(city != null && !obj.getLocation().equalsIgnoreCase(city)) {
                        addToList = false;
                    }

                    if(durationType != null && obj.getDuration() != durationType) {
                        addToList = false;
                    }

                    if (addToList) {
                        list.add(obj);
                        UserRepository.getInstance().getById(obj.getEmployerId()).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                obj.setEmployer(task1.getResult().toObject(Employer.class));
                                offers.postValue(list);
                            } else {
                                errorMessage.postValue(R.string.error_loading_users);
                                if (task1.getException() != null) {
                                    task1.getException().printStackTrace();
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

    public void addOffer(Offer offer) {
        OfferRepository.getInstance().add(offer).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                offer.setId(task.getResult().getId());
                this.offer.postValue(offer);
                List<Offer> offers = this.offers.getValue() == null ? new ArrayList<>() : this.offers.getValue();
                offers.add(offer);
                this.offers.postValue(offers);
            } else {
                errorMessage.postValue(R.string.error_adding_offer);
                if (task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
    }

    public void addApplication(String userId, Offer offer) {
        if(offer.getApplications().containsKey(userId)) {
            return;
        }
        offer.getApplications().put(userId, PENDING);
        OfferRepository.getInstance().update(offer.getId(), "applications", offer.getApplications()).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                this.offer.postValue(offer);
            } else {
                offer.getApplications().remove(userId);
                errorMessage.postValue(R.string.error_adding_applications);
                if (task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
    }

    public MutableLiveData<List<Offer>> getOffers() {
        return offers;
    }

    public MutableLiveData<Offer> getOffer() {
        return offer;
    }

    public MutableLiveData<Integer> getError() {
        return errorMessage;
    }
}
