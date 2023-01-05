package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.example.jobretriever.viewmodels.UserViewModel;

import java.util.List;


public class OfferFragment extends Fragment {
    View view;
    Offer offer;

    public OfferFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.details);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offer, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(offerExists()) {
            fillLayoutData();

            ImageButton favoriteButton = view.findViewById(R.id.offer_favorite);
            Button applyButton = view.findViewById(R.id.offer_apply);

            applyButton.setOnClickListener(v -> applyToOffer());
            favoriteButton.setOnClickListener(v -> toggleFavorite());
        } else {
            goToFragment(HomeFragment.class);
            Toast.makeText(getContext(), R.string.error_offer_not_found, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public boolean offerExists() {
        Bundle args = this.getArguments();
        List<Offer> offers = OfferViewModel.getInstance().getOffers().getValue();
        if(args != null && offers != null && args.getInt("offerIndex", -1) != -1) {
            int index = args.getInt("offerIndex");
            offer = offers.get(index);
            return true;
        }
        return false;
    }

    public void fillLayoutData() {
        TextView jobTitleTV = view.findViewById(R.id.offer_job_title);
        TextView businessNameTV = view.findViewById(R.id.offer_busines_name);
        TextView durationTV = view.findViewById(R.id.offer_duration);
        TextView descriptionTV = view.findViewById(R.id.offer_description);
        TextView cityCountry = view.findViewById(R.id.offer_location);
        TextView wage = view.findViewById(R.id.offer_wage);
        ImageButton favoriteButton = view.findViewById(R.id.offer_favorite);
        int favoriteImage = UserViewModel.getInstance().hasFavorite(offer.getId()) ?
                R.drawable.ic_baseline_favorite_24 :
                R.drawable.ic_baseline_favorite_border_24;

        jobTitleTV.setText(offer.getTitle());
        businessNameTV.setText(offer.getEmployer().getBusinessName());
        durationTV.setText(offer.getDuration());
        descriptionTV.setText(offer.getDescription());
        cityCountry.setText(String.format(getString(R.string.offer_location), offer.getLocation().getName(), offer.getLocation().getCountry()));
        wage.setText(String.format(getString(R.string.offer_wage), offer.getWage()));
        favoriteButton.setImageResource(favoriteImage);
    }

    public void applyToOffer() {
        if (UserViewModel.getInstance().isLoggedIn()) {
            goToFragment(ApplyFragment.class);
        }else {
            Toast.makeText(getContext(), R.string.error_must_be_signed_in , Toast.LENGTH_LONG).show();
        }

    }

    public void toggleFavorite() {
        ImageButton favoriteButton = view.findViewById(R.id.offer_favorite);
        if (UserViewModel.getInstance().isLoggedIn()) {
            if(UserViewModel.getInstance().hasFavorite(offer.getId())) {
                UserViewModel.getInstance().removeFavorite(offer.getId());
                favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            } else {
                UserViewModel.getInstance().addFavorite(offer.getId());
                favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
            }
        } else {
            Toast.makeText(getContext(), R.string.error_must_be_signed_in , Toast.LENGTH_LONG).show();
        }
    }

    public void goToFragment(Class<? extends Fragment> fragmentClass) {
        if(getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentClass, null).commit();
        }
    }
}
