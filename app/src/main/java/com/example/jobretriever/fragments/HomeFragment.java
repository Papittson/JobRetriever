package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.models.DurationType;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;

import java.util.List;


public class HomeFragment extends JRFragment {
    DurationType durationType;

    public HomeFragment() {
        super(R.string.welcome_message, R.layout.fragment_home, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button confirmSearchButton = fragment.findViewById(R.id.confirmSearch);
        AutoCompleteTextView cityFilterInput = fragment.findViewById(R.id.city_filter);
        AutoCompleteTextView durationFilterInput = fragment.findViewById(R.id.duration_filter);

        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, getCities());
        cityFilterInput.setAdapter(citiesAdapter);
        cityFilterInput.setThreshold(1);

        ArrayAdapter<DurationType> durationsAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, DurationType.values());
        durationFilterInput.setAdapter(durationsAdapter);

        durationFilterInput.setOnItemClickListener((parent, arg1, position, arg3) -> {
            Object item = parent.getItemAtPosition(position);
            if (item instanceof DurationType) {
                durationType = (DurationType) item;
            }
        });

        confirmSearchButton.setOnClickListener(_view -> search());
    }

    @Override
    public void onStart() {
        super.onStart();
        createRecyclerView(R.id.welcomeOfferRV);

        List<Offer> offersLiveData = OfferViewModel.getInstance().getOffers().getValue();
        if(offersLiveData == null || offersLiveData.size() == 0) {
            OfferViewModel.getInstance().getAll();
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

    public void search() {
        EditText searchEditText = fragment.findViewById(R.id.search_bar);
        EditText cityEditText = fragment.findViewById(R.id.city_filter);

        if(searchEditText != null && cityEditText != null) {
            String searchQuery = searchEditText.getText().toString();
            String city = cityEditText.getText().toString();
            OfferViewModel.getInstance().getAll(searchQuery, city, durationType);
        }
    }
}