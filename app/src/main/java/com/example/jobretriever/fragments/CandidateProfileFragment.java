package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.models.UserType;
import com.example.jobretriever.viewmodels.OfferViewModel;

import java.util.List;
import java.util.stream.Collectors;


public class CandidateProfileFragment extends JRFragment {

    public CandidateProfileFragment() {
        super(R.string.profile, null, true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if(user != null) {
            fragment = inflater.inflate(getFragmentLayout(user.getType()), container, false);
        }
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        if(user != null) {
            TextView hello = fragment.findViewById(R.id.helloCandidate);
            hello.setText(String.format(getResources().getString(R.string.hello_candidate_fragment), user.getFirstname(), user.getName()));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        createRecyclerView(R.id.saved_offers);

        if(user != null) {
            List<Offer> offersLiveData = OfferViewModel.getInstance().getOffers().getValue();
            if(offersLiveData == null || offersLiveData.size() == 0) {
                OfferViewModel.getInstance().getAll(null);
            }

            OfferViewModel.getInstance().getOffers().observe(
                    getViewLifecycleOwner(),
                    offers -> {
                        List<Offer> favoriteOffers = offers.stream()
                                .filter(offer -> user.getFavoritesId().contains(offer.getId()))
                                .collect(Collectors.toList());
                        updateRecyclerView(R.id.welcomeOfferRV, favoriteOffers);
                    }
            );
        }
    }

    private int getFragmentLayout(UserType userType) {
        switch (userType) {
            case APPLICANT:
                return R.layout.fragment_candidate_home;
            case EMPLOYER:
                return R.layout.fragment_candidate_home;
            case AGENCY:
                return R.layout.fragment_candidate_home;
            case MODERATOR:
                return R.layout.fragment_candidate_home;
            default:
                return R.layout.fragment_candidate_home;
        }
    }
}