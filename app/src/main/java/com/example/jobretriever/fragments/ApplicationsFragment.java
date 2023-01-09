package com.example.jobretriever.fragments;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;

import java.util.List;
import java.util.stream.Collectors;


public class ApplicationsFragment extends JRFragment {

    public ApplicationsFragment() {
        super(R.string.applications, R.layout.fragment_applications, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isUserAllowed()) {
            return;
        }

        createRecyclerView(R.id.applied_offers);

        if (user != null) {
            List<Offer> offersLiveData = OfferViewModel.getInstance().getOffers().getValue();
            if (offersLiveData == null || offersLiveData.size() == 0) {
                OfferViewModel.getInstance().getAll();
            }

            OfferViewModel.getInstance().getOffers().observe(
                    getViewLifecycleOwner(),
                    offers -> {
                        List<Offer> appliedOffers;
                        if(user instanceof Employer) {
                            appliedOffers = offers.stream()
                                    .filter(offer -> offer.isCreatedByUser(user.getId()))
                                    .collect(Collectors.toList());
                        } else {
                            appliedOffers = offers.stream()
                                    .filter(offer -> offer.isAppliedByUser(user.getId()))
                                    .collect(Collectors.toList());
                        }

                        updateRecyclerView(R.id.applied_offers, appliedOffers);
                    }
            );
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getOffers().removeObservers(getViewLifecycleOwner());
    }
}