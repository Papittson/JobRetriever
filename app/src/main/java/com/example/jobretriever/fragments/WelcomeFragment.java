package com.example.jobretriever.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.R;
import com.example.jobretriever.adapters.OffersAdapter;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;


public class WelcomeFragment extends Fragment {
    View view;

    public WelcomeFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.welcome_message);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome, container, false);
        Button confirm = (Button) view.findViewById(R.id.confirmSearch);
        confirm.setOnClickListener(_view -> {
            TextInputLayout searchBar = view.findViewById(R.id.search_bar);
            EditText editText = searchBar.getEditText();
            if(editText != null) {
                String searchQuery = editText.getText().toString();
                OfferViewModel.getInstance().getAll(searchQuery);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = view.findViewById(R.id.welcomeOfferRV);
        ArrayList<Offer> offers = new ArrayList<>();
        OffersAdapter adapter = new OffersAdapter(getContext(), getActivity(), offers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        OfferViewModel.getInstance().getOffers().observe(
                getViewLifecycleOwner(),
                offerList -> {
                    offers.clear();
                    offers.addAll(offerList);
                    recyclerView.setAdapter(adapter);
                    System.out.println("Les offres : " + offers);
                }
        );
        OfferViewModel.getInstance().getError().observe(
                getViewLifecycleOwner(),
                errorMessage -> Toast.makeText((Context) getViewLifecycleOwner(), errorMessage, Toast.LENGTH_LONG).show()
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getOffers().removeObservers(getViewLifecycleOwner());
        OfferViewModel.getInstance().getError().removeObservers(getViewLifecycleOwner());
    }
}