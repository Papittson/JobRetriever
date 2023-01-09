package com.example.jobretriever.viewmodels;

import static com.example.jobretriever.enums.ApplicationStatus.PENDING;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jobretriever.R;
import com.example.jobretriever.enums.DurationType;
import com.example.jobretriever.models.Applicant;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.repositories.OfferRepository;
import com.example.jobretriever.repositories.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OfferViewModel extends ViewModel {
    private static OfferViewModel instance;
    private final MutableLiveData<Integer> errorMessage = new MutableLiveData<>(null);
    private final MutableLiveData<Offer> selectedOffer = new MutableLiveData<>(null);
    private final MutableLiveData<Offer> appliedOffer = new MutableLiveData<>(null);
    private final MutableLiveData<List<Offer>> appliedOffers = new MutableLiveData<>(null);
    private final MutableLiveData<List<Offer>> recentOffers = new MutableLiveData<>(null);
    private final MutableLiveData<List<Offer>> searchedOffers = new MutableLiveData<>(null);
    private final MutableLiveData<List<Offer>> savedOffers = new MutableLiveData<>(null);
    private final MutableLiveData<List<Offer>> availableOffers = new MutableLiveData<>(null);

    private OfferViewModel() {
    }

    public static OfferViewModel getInstance() {
        if (instance == null) {
            instance = new OfferViewModel();
        }
        return instance;
    }

    private void request(Task<QuerySnapshot> query, MutableLiveData<List<Offer>> liveData) {
        request(query, liveData, null);
    }

    private void request(Task<QuerySnapshot> query, MutableLiveData<List<Offer>> liveData, String searchQuery) {
        query.addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                if(task.getResult().isEmpty()) {
                    liveData.postValue(new ArrayList<>());
                    return;
                }
                int documentsSize = task.getResult().size();
                List<Offer> offers = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Offer offer = document.toObject(Offer.class);
                    if(searchQuery == null || offer.isSimilarTo(searchQuery)) {
                        UserRepository.getInstance().getById(offer.getEmployerId()).addOnCompleteListener(task1 -> {
                            if(task1.isSuccessful()) {
                                Employer employer = task1.getResult().toObject(Employer.class);
                                if(employer != null) {
                                    offer.setEmployer(employer);
                                    offers.add(offer);
                                    if(offers.size() == documentsSize) {
                                        if(searchQuery != null) {
                                            List<Offer> filteredOffers = offers.stream()
                                                    .filter(_offer -> _offer.isSimilarTo(searchQuery))
                                                    .collect(Collectors.toList());
                                            liveData.postValue(filteredOffers);
                                        } else {
                                            liveData.postValue(offers);
                                        }
                                    }
                                } else {
                                    errorMessage.postValue(R.string.error_loading_users);
                                }
                            } else {
                                errorMessage.postValue(R.string.error_loading_users);
                            }
                        });
                    } else {
                        offers.add(offer);
                        if(offers.size() == documentsSize) {
                            List<Offer> filteredOffers = offers.stream()
                                    .filter(_offer -> _offer.isSimilarTo(searchQuery))
                                    .collect(Collectors.toList());
                            liveData.postValue(filteredOffers);
                            return;
                        }
                    }
                }
            } else {
                errorMessage.postValue(R.string.error_loading_offers);
                if(task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
    }

    public void getAll(long limit) {
        request(OfferRepository.getInstance().getAll(limit), this.recentOffers);
    }

    public void getFavorites(List<String> favoritesId) {
        if(!favoritesId.isEmpty()) {
            request(OfferRepository.getInstance().getFavorites(favoritesId), this.savedOffers);
        }
    }

    public void getApplieds(List<String> applicationsId) {
        if(!applicationsId.isEmpty()) {
            request(OfferRepository.getInstance().getApplieds(applicationsId), this.appliedOffers);
        }
    }

    public void getAvailables(String userId) {
        request(OfferRepository.getInstance().getAvailables(userId), this.availableOffers);
    }

    public void search(String searchQuery, String city, DurationType durationType) {
        request(OfferRepository.getInstance().search(city, durationType), this.searchedOffers, searchQuery);
    }

    public void addOffer(Offer offer) {
        OfferRepository.getInstance().add(offer).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                this.selectedOffer.postValue(offer);
            } else {
                errorMessage.postValue(R.string.error_adding_offer);
            }
        });
    }

    public void addApplication(Applicant applicant, Offer offer) {
        if(offer.getApplications().containsKey(applicant.getId())) {
            return;
        }
        offer.getApplications().put(applicant.getId(), PENDING);
        applicant.getApplicationsId().add(offer.getId());
        OfferRepository.getInstance().update(offer.getId(), "applications", offer.getApplications()).addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                UserRepository.getInstance().update(applicant.getId(), "applicationsId", applicant.getApplicationsId()).addOnCompleteListener(task1 -> {
                    if(task1.isSuccessful()) {
                        this.appliedOffer.postValue(offer);
                    } else {
                        offer.getApplications().remove(applicant.getId());
                        applicant.getApplicationsId().remove(offer.getId());
                        errorMessage.postValue(R.string.error_adding_applications);
                    }
                });
            } else {
                offer.getApplications().remove(applicant.getId());
                applicant.getApplicationsId().remove(offer.getId());
                errorMessage.postValue(R.string.error_adding_applications);
            }
        });
    }

    public MutableLiveData<List<Offer>> getAppliedOffers() {
        return appliedOffers;
    }

    public MutableLiveData<List<Offer>> getRecentOffers() {
        return recentOffers;
    }

    public MutableLiveData<List<Offer>> getSearchedOffers() {
        return searchedOffers;
    }

    public MutableLiveData<List<Offer>> getSavedOffers() {
        return savedOffers;
    }

    public MutableLiveData<List<Offer>> getAvailableOffers() {
        return availableOffers;
    }

    public MutableLiveData<Offer> getAppliedOffer() {
        return appliedOffer;
    }

    public MutableLiveData<Offer> getSelectedOffer() {
        return selectedOffer;
    }

    public MutableLiveData<Integer> getError() {
        return errorMessage;
    }
}
