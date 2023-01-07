package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;


public class HomeFragment extends JRFragment {

    public HomeFragment() {
        super(R.string.welcome_message, R.layout.fragment_home, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button confirmSearchButton = view.findViewById(R.id.confirmSearch);
        confirmSearchButton.setOnClickListener(_view -> {
            TextInputLayout searchBar = view.findViewById(R.id.search_bar);
            EditText editText = searchBar.getEditText();
            if(editText != null) {
                String searchQuery = editText.getText().toString();
                OfferViewModel.getInstance().getAll(searchQuery);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        createRecyclerView(R.id.welcomeOfferRV);

        List<Offer> offersLiveData = OfferViewModel.getInstance().getOffers().getValue();
        if(offersLiveData == null || offersLiveData.size() == 0) {
            OfferViewModel.getInstance().getAll(null);
        }

        OfferViewModel.getInstance().getOffers().observe(
                getViewLifecycleOwner(),
                offers -> updateRecyclerView(R.id.welcomeOfferRV, offers)
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getOffers().removeObservers(getViewLifecycleOwner());
    }
}